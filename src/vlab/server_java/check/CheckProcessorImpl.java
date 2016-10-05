package vlab.server_java.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import rlcp.check.ConditionForChecking;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.check.PreCheckProcessor.PreCheckResult;
import rlcp.server.processor.check.PreCheckResultAwareCheckProcessor;
import vlab.server_java.model.Solution;
import vlab.server_java.model.Variant;
import vlab.server_java.model.tool.ToolModel;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static vlab.server_java.model.util.Util.bd;
import static vlab.server_java.model.util.Util.prepareInputJsonString;

/**
 * Simple CheckProcessor implementation. Supposed to be changed as needed to provide
 * necessary Check method support.
 */
public class CheckProcessorImpl implements PreCheckResultAwareCheckProcessor<String> {
    @Override
    public CheckingSingleConditionResult checkSingleCondition(ConditionForChecking condition, String instructions, GeneratingResult generatingResult) throws Exception {
        //do check logic here
        BigDecimal points = ZERO;
        String comment = "";

        try{
            ObjectMapper mapper = new ObjectMapper();

            Solution solution = mapper.readValue(prepareInputJsonString(instructions), Solution.class);
            Variant variant = mapper.readValue(prepareInputJsonString(generatingResult.getCode()), Variant.class);
            String[] secret = prepareInputJsonString(generatingResult.getInstructions()).split(":");
            BigDecimal realK = new BigDecimal(secret[0]);
            BigDecimal realN = new BigDecimal(secret[1]);
            BigDecimal realQ = variant.getNeeded_Q();

            BigDecimal solutionQ = ToolModel.getQ(solution.getDelta_p(), solution.getTube_radius(), variant.getTube_length(), realK, realN);
            BigDecimal solutionK = solution.getK();
            BigDecimal solutionN = solution.getN();

            boolean isKOk = realK.subtract(solutionK).abs().compareTo(bd(0.01)) <= 0;
            boolean isNOk = realN.subtract(solutionN).abs().compareTo(bd(0.01)) <= 0;
            boolean isQOk = realQ.subtract(solutionQ).abs().compareTo(realQ.multiply(bd(0.01))) <= 0;

            if(isKOk && isNOk){
                if (isQOk){
                    points = ONE;
                    comment = "Ok";
                } else {
                    points = bd(0.3);
                    comment = "Ошибка при конфигурировании трубы. Полученный объёмный расход Q составляет: "
                            + solutionQ
                            + ", а целевой объёмный расход составляет: " + variant.getNeeded_Q();
                }
            } else {
                points = ZERO;
                comment = "Неверно рассчитаны коэффициенты. Полученные значения: "
                        + solution.getK() + "; " + solution.getN()
                        + ", а верные значения составляют: "
                        + realK + "; " + realN;
            }

        } catch (Exception e){
            points = ZERO;
            comment = e.getMessage();
        }

        return new CheckingSingleConditionResult(points, comment);
    }

    @Override
    public void setPreCheckResult(PreCheckResult<String> preCheckResult) {}
}

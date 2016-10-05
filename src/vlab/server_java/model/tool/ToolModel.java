package vlab.server_java.model.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.*;
import static vlab.server_java.model.util.Util.bd;
import static vlab.server_java.model.util.Util.escapeParam;

/**
 * Created by efimchick on 04.07.16.
 */
public class ToolModel {

    public static BigDecimal getQ(
            BigDecimal delta_p,
            BigDecimal tube_radius,
            BigDecimal tube_length,
            BigDecimal k,
            BigDecimal n
    ){

        try {


            double onePlus3N = 1 + 3 * n.doubleValue();

            if (n.compareTo(ZERO) == 0
                    || n.compareTo(ZERO) == 0
                    || k.compareTo(ZERO) == 0
                    || tube_length.compareTo(ZERO) == 0
                    || onePlus3N == 0
                    ) {
                return ZERO;
            }

            //-(p/2Lk)^(1/n)*Pi*n/(1+3n)*R^(1+3n/n)

            double firstMultiplier = Math.pow(
                    delta_p.doubleValue() * 1000 / (2 * tube_length.doubleValue() + k.doubleValue()),
                    1 / n.doubleValue()
            );

            double secondMultiplier = PI * n.doubleValue() / onePlus3N;
            double thirdMultiplier = Math.pow(tube_radius.doubleValue(), onePlus3N / n.doubleValue());

            return bd(firstMultiplier * secondMultiplier * thirdMultiplier);
        }
        catch (Exception e){
            e.printStackTrace();
            return ZERO;
        }
    }
}

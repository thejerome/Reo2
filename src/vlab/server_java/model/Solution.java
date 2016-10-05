package vlab.server_java.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static vlab.server_java.model.util.Util.shrink;

/**
 *  {mu_values: [0, 1, 2, 1, 3], k: 1.33, delta_p: 300, tube_radius: 0.03}
 */
public class Solution {

    private final BigDecimal k;
    private final BigDecimal n;
    private final BigDecimal delta_p;
    private final BigDecimal tube_radius;

    @JsonCreator
    public Solution(
            @JsonProperty("k") BigDecimal k,
            @JsonProperty("n") BigDecimal n,
            @JsonProperty("delta_p") BigDecimal delta_p,
            @JsonProperty("tube_radius") BigDecimal tube_radius) {
        this.k = shrink(k);
        this.n = shrink(n);
        this.delta_p = shrink(delta_p);
        this.tube_radius = shrink(tube_radius);
    }

    public BigDecimal getK() {
        return k;
    }

    public BigDecimal getDelta_p() {
        return delta_p;
    }

    public BigDecimal getTube_radius() {
        return tube_radius;
    }

    public BigDecimal getN() {
        return n;
    }
}

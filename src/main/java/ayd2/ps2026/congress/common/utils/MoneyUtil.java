package ayd2.ps2026.congress.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyUtil {

    private MoneyUtil() { }

    public static Double round(Double number){
        return BigDecimal
                .valueOf(number)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Float roundTwoDecimals(Double number){
        return BigDecimal
                .valueOf(number)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
    }

}

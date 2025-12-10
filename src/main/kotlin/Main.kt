fun main() {

    val ctrl = FuelData(
        Hp = 1.9, Cp = 21.1, Sp = 2.6, Np = 0.2, Op = 7.1, Wp = 53.0, Ap = 14.1, name="Контрольний приклад"
    )

    //варіант №5:
    val mine = FuelData(
        Hp = 2.8, Cp = 72.3, Sp = 2.0, Np = 1.1, Op = 1.3, Wp = 5.5, Ap = 15.0, name="Варіант №5"
    )

    listOf(ctrl, mine).forEach { d ->
        val r = calculate(d)
        println("=== ${d.name} ===")
        println("Вхідні (%) — H:${d.Hp}, C:${d.Cp}, S:${d.Sp}, N:${d.Np}, O:${d.Op}, W:${d.Wp}, A:${d.Ap}")
        println("Krs (робоча->суха) = ${"%.4f".format(r.Krs)}")
        println("Krg (робоча->горюча) = ${"%.4f".format(r.Krg)}")
        println("--- Склад сухої маси (%) ---")
        println("H_s=${"%.3f".format(r.Hs)}; C_s=${"%.3f".format(r.Cs)}; S_s=${"%.3f".format(r.Ss)}; N_s=${"%.3f".format(r.Ns)}; O_s=${"%.3f".format(r.Os)}; A_s=${"%.3f".format(r.As)}")
        println("Сума (перевірка) = ${"%.6f".format(r.sumS)}%")
        println("--- Склад горючої маси (%) ---")
        println("H_g=${"%.3f".format(r.Hg)}; C_g=${"%.3f".format(r.Cg)}; S_g=${"%.3f".format(r.Sg)}; N_g=${"%.3f".format(r.Ng)}; O_g=${"%.3f".format(r.Og)}")
        println("Сума (перевірка) = ${"%.6f".format(r.sumG)}%")
        println("--- Нижча теплота згоряння ---")
        println("Qr (робоча) = ${"%.6f".format(r.Qr_MJ)} МДж/кг")
        println("Qd (суха)   = ${"%.6f".format(r.Qd_MJ)} МДж/кг")
        println("Qdaf (горюча) = ${"%.6f".format(r.Qdaf_MJ)} МДж/кг")
        println()
    }
}

data class FuelData(
    val Hp: Double, val Cp: Double, val Sp: Double,
    val Np: Double, val Op: Double, val Wp: Double, val Ap: Double,
    val name: String = ""
)

data class Result(
    val Krs: Double, val Krg: Double,
    val Hs: Double, val Cs: Double, val Ss: Double, val Ns: Double, val Os: Double, val As: Double, val sumS: Double,
    val Hg: Double, val Cg: Double, val Sg: Double, val Ng: Double, val Og: Double, val sumG: Double,
    val Qr_MJ: Double, val Qd_MJ: Double, val Qdaf_MJ: Double
)

fun calculate(d: FuelData): Result {
    val Krs = 100.0 / (100.0 - d.Wp)
    val Krg = 100.0 / (100.0 - d.Wp - d.Ap)

    val Hs = d.Hp * Krs
    val Cs = d.Cp * Krs
    val Ss = d.Sp * Krs
    val Ns = d.Np * Krs
    val Os = d.Op * Krs
    val As = d.Ap * Krs
    val sumS = Hs + Cs + Ss + Ns + Os + As

    val Hg = d.Hp * Krg
    val Cg = d.Cp * Krg
    val Sg = d.Sp * Krg
    val Ng = d.Np * Krg
    val Og = d.Op * Krg
    val sumG = Hg + Cg + Sg + Ng + Og

    // Менделєєв (кДж/кг)
    val Qr_kJ = 339.0 * d.Cp + 1030.0 * d.Hp - 108.8 * (d.Op - d.Sp) - 25.0 * d.Wp
    val Qr_MJ = Qr_kJ / 1000.0
    val Qd_MJ = Qr_MJ * Krs
    val Qdaf_MJ = Qr_MJ * Krg

    return Result(
        Krs, Krg,
        Hs, Cs, Ss, Ns, Os, As, sumS,
        Hg, Cg, Sg, Ng, Og, sumG,
        Qr_MJ, Qd_MJ, Qdaf_MJ
    )
}

package aoc2021.day24

class ALUImpl {

    var x = 1
    var y = 0
    var z = 0
    var w = 0

    fun run(input: List<Int>): Int {

        z = (input[0] + 4) * 26 // z % 26 = 0

        w = input[6] //input[1] - 1 == input[6]
        w = input[9] // input[0] - 5 == input[9]
        if((z % 26) - 5 != w) {
            z *= 26
            z += w + 11
        }
        z /= 26

        w = input[10]
        if((z % 26) - 9 != w) {
            z *= 26
            z += w + 8
        }
        z /= 26

        w = input[11]
        if((z % 26) - 5 != w) {
            z *= 26
            z += w + 3
        }
        z /= 26

        w = input[12]
        if((z % 26) - 2 != w) {
            z *= 26
            z += w + 1
        }
        z /= 26

        w = input[13]
        if((z % 26) - 7 != w) {
            z *= 26
            z += w + 8
        }
        z /= 26

        return z
    }
}
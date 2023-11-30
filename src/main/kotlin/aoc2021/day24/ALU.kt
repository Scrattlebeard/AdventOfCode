package aoc2021.day24

class ALUTemplate {

    var x = 0
    var y = 0
    var z = 0
    var w = 0

    fun run(input: List<Int>): Int {

        w = input[0]
x = 0
x += z
x %= 26

x += 12
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 4
y *= x
z += y
w = input[1]
x = 0
x += z
x %= 26

x += 11
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 10
y *= x
z += y
w = input[2]
x = 0
x += z
x %= 26

x += 14
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 12
y *= x
z += y
w = input[3]
x = 0
x += z
x %= 26
z /= 26
x += -6
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 14
y *= x
z += y
w = input[4]
x = 0
x += z
x %= 26

x += 15
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 6
y *= x
z += y
w = input[5]
x = 0
x += z
x %= 26

x += 12
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 16
y *= x
z += y
w = input[6]
x = 0
x += z
x %= 26
z /= 26
x += -9
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 1
y *= x
z += y
w = input[7]
x = 0
x += z
x %= 26

x += 14
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 7
y *= x
z += y
w = input[8]
x = 0
x += z
x %= 26

x += 14
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 8
y *= x
z += y
w = input[9]
x = 0
x += z
x %= 26
z /= 26
x += -5
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 11
y *= x
z += y
w = input[10]
x = 0
x += z
x %= 26
z /= 26
x += -9
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 8
y *= x
z += y
w = input[11]
x = 0
x += z
x %= 26
z /= 26
x += -5
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 3
y *= x
z += y
w = input[12]
x = 0
x += z
x %= 26
z /= 26
x += -2
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 1
y *= x
z += y
w = input[13]
x = 0
x += z
x %= 26
z /= 26
x += -7
x = if(x == w) 1 else 0
x = if(x == 0) 1 else 0
y = 0
y += 25
y *= x
y += 1
z *= y
y = 0
y += w
y += 8
y *= x
z += y


        return z
    }
}
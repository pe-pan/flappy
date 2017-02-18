package net.panuska.tlappy;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2017.
 */
public class Fonts {
    static byte[][] bitmap = {
        {                      // letter   - (0x20)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter ! - (0x21)
                (byte) 0b00011000,
                (byte) 0b00011000,
                (byte) 0b00011000,
                (byte) 0b00011000,
                (byte) 0b00011000,
                (byte) 0b00000000,
                (byte) 0b00011000,
                (byte) 0b00000000
        }, {                   // letter " - (0x22)
                (byte) 0b01101100,
                (byte) 0b01101100,
                (byte) 0b00100100,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter # - (0x23)
                (byte) 0b01101100,
                (byte) 0b01101100,
                (byte) 0b11111110,
                (byte) 0b01101100,
                (byte) 0b11111110,
                (byte) 0b01101100,
                (byte) 0b01101100,
                (byte) 0b00000000
        }, {                   // letter $ - (0x24)
                (byte) 0b00011000,
                (byte) 0b00111110,
                (byte) 0b01111000,
                (byte) 0b00111100,
                (byte) 0b00011110,
                (byte) 0b01111100,
                (byte) 0b00011000,
                (byte) 0b00000000
        }, {                   // letter % - (0x25)
                (byte) 0b00000000,
                (byte) 0b11000110,
                (byte) 0b11001100,
                (byte) 0b00011000,
                (byte) 0b00110000,
                (byte) 0b01100110,
                (byte) 0b11000110,
                (byte) 0b00000000
        }, {                   // letter & - (0x26)
                (byte) 0b01110000,
                (byte) 0b11011000,
                (byte) 0b11011000,
                (byte) 0b01110000,
                (byte) 0b11011110,
                (byte) 0b11011100,
                (byte) 0b01110110,
                (byte) 0b00000000
        }, {                   // letter ' - (0x27)
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00011000,
                (byte) 0b00110000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter ( - (0x28)
                (byte) 0b00001000,
                (byte) 0b00110000,
                (byte) 0b01100000,
                (byte) 0b01100000,
                (byte) 0b01100000,
                (byte) 0b00110000,
                (byte) 0b00001000,
                (byte) 0b00000000
        }, {                   // letter ) - (0x29)
                (byte) 0b00100000,
                (byte) 0b00011000,
                (byte) 0b00001100,
                (byte) 0b00001100,
                (byte) 0b00001100,
                (byte) 0b00011000,
                (byte) 0b00100000,
                (byte) 0b00000000
        }, {                   // letter * - (0x2a)
                (byte) 0b00000000,
                (byte) 0b00011000,
                (byte) 0b01011010,
                (byte) 0b00111100,
                (byte) 0b00111100,
                (byte) 0b01011010,
                (byte) 0b00011000,
                (byte) 0b00000000
        }, {                   // letter + - (0x2b)
                (byte) 0b00000000,
                (byte) 0b00011000,
                (byte) 0b00011000,
                (byte) 0b01111110,
                (byte) 0b01111110,
                (byte) 0b00011000,
                (byte) 0b00011000,
                (byte) 0b00000000
        }, {                   // letter , - (0x2c)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00011000,
                (byte) 0b00110000
        }, {                   // letter - - (0x2d)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111100,
                (byte) 0b01111100,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter . - (0x2e)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter / - (0x2f)
                (byte) 0b00000000,
                (byte) 0b00000110,
                (byte) 0b00001100,
                (byte) 0b00011000,
                (byte) 0b00110000,
                (byte) 0b01100000,
                (byte) 0b11000000,
                (byte) 0b00000000
        }, {                   // letter 0 - (0x30)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter 1 - (0x31)
                (byte) 0b00111000,
                (byte) 0b01111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter 2 - (0x32)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b00001110,
                (byte) 0b00111100,
                (byte) 0b01110000,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b00000000
        }, {                   // letter 3 - (0x33)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b00001110,
                (byte) 0b00111100,
                (byte) 0b00001110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter 4 - (0x34)
                (byte) 0b00111100,
                (byte) 0b01111100,
                (byte) 0b11011100,
                (byte) 0b11011110,
                (byte) 0b11111110,
                (byte) 0b00011100,
                (byte) 0b00011100,
                (byte) 0b00000000
        }, {                   // letter 5 - (0x35)
                (byte) 0b11111110,
                (byte) 0b11100000,
                (byte) 0b11111100,
                (byte) 0b00001110,
                (byte) 0b00001110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter 6 - (0x36)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b11100000,
                (byte) 0b11111100,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter 7 - (0x37)
                (byte) 0b11111110,
                (byte) 0b11101110,
                (byte) 0b00001110,
                (byte) 0b00011100,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000
        }, {                   // letter 8 - (0x38)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter 9 - (0x39)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b01111110,
                (byte) 0b00001110,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter : - (0x3a)
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter ; - (0x3b)
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00011000,
                (byte) 0b00110000
        }, {                   // letter < - (0x3c)
                (byte) 0b00011110,
                (byte) 0b00111000,
                (byte) 0b01110000,
                (byte) 0b11100000,
                (byte) 0b01110000,
                (byte) 0b00111000,
                (byte) 0b00011110,
                (byte) 0b00000000
        }, {                   // letter = - (0x3d)
                (byte) 0b00000000,
                (byte) 0b01111110,
                (byte) 0b01111110,
                (byte) 0b00000000,
                (byte) 0b01111110,
                (byte) 0b01111110,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter > - (0x3e)
                (byte) 0b01111000,
                (byte) 0b00011100,
                (byte) 0b00001110,
                (byte) 0b00000111,
                (byte) 0b00001110,
                (byte) 0b00011100,
                (byte) 0b01111000,
                (byte) 0b00000000
        }, {                   // letter ? - (0x3f)
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b00001110,
                (byte) 0b00011100,
                (byte) 0b00111000,
                (byte) 0b00000000,
                (byte) 0b00111000,
                (byte) 0b00111000
        }, {                   // letter @ - (0x40)
                (byte) 0b01111100,
                (byte) 0b11000110,
                (byte) 0b00000110,
                (byte) 0b01100110,
                (byte) 0b11010110,
                (byte) 0b11010110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter A - (0x41)
                (byte) 0b01111100,
                (byte) 0b11111110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b00000000
        }, {                   // letter B - (0x42)
                (byte) 0b11111100,
                (byte) 0b01111110,
                (byte) 0b01100110,
                (byte) 0b01111100,
                (byte) 0b01100110,
                (byte) 0b01111110,
                (byte) 0b11111100,
                (byte) 0b00000000
        }, {                   // letter C - (0x43)
                (byte) 0b01111100,
                (byte) 0b11111110,
                (byte) 0b11100110,
                (byte) 0b11100000,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter D - (0x44)
                (byte) 0b11111100,
                (byte) 0b01111110,
                (byte) 0b01100110,
                (byte) 0b01100110,
                (byte) 0b01100110,
                (byte) 0b01111110,
                (byte) 0b11111100,
                (byte) 0b00000000
        }, {                   // letter E - (0x45)
                (byte) 0b01111110,
                (byte) 0b11111110,
                (byte) 0b11111000,
                (byte) 0b11111110,
                (byte) 0b11111000,
                (byte) 0b11111110,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter F - (0x46)
                (byte) 0b01111110,
                (byte) 0b11111110,
                (byte) 0b11111000,
                (byte) 0b11111110,
                (byte) 0b11111000,
                (byte) 0b11111000,
                (byte) 0b01111000,
                (byte) 0b00000000
        }, {                   // letter G - (0x47)
                (byte) 0b01111100,
                (byte) 0b11100110,
                (byte) 0b11100000,
                (byte) 0b11101110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter H - (0x48)
                (byte) 0b01100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b00000000
        }, {                   // letter I - (0x49)
                (byte) 0b01111110,
                (byte) 0b00111100,
                (byte) 0b00111100,
                (byte) 0b00111100,
                (byte) 0b00111100,
                (byte) 0b00111100,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter J - (0x4a)
                (byte) 0b00001110,
                (byte) 0b00001110,
                (byte) 0b00001110,
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter K - (0x4b)
                (byte) 0b11101110,
                (byte) 0b11111110,
                (byte) 0b11111100,
                (byte) 0b11111100,
                (byte) 0b11111100,
                (byte) 0b11111110,
                (byte) 0b11101110,
                (byte) 0b00000000
        }, {                   // letter L - (0x4c)
                (byte) 0b01110000,
                (byte) 0b11110000,
                (byte) 0b11110000,
                (byte) 0b11110000,
                (byte) 0b11110000,
                (byte) 0b11111110,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter M - (0x4d)
                (byte) 0b11000110,
                (byte) 0b11101110,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b11010110,
                (byte) 0b11000110,
                (byte) 0b00000000
        }, {                   // letter N - (0x4e)
                (byte) 0b11100110,
                (byte) 0b11110110,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b11101110,
                (byte) 0b11100110,
                (byte) 0b00000000
        }, {                   // letter O - (0x4f)
                (byte) 0b01111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter P - (0x50)
                (byte) 0b11111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b11111100,
                (byte) 0b11100000,
                (byte) 0b11100000,
                (byte) 0b00000000
        }, {                   // letter Q - (0x51)
                (byte) 0b01111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11101110,
                (byte) 0b11111100,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter R - (0x52)
                (byte) 0b11111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b11111100,
                (byte) 0b11111110,
                (byte) 0b11101110,
                (byte) 0b00000000
        }, {                   // letter S - (0x53)
                (byte) 0b01111100,
                (byte) 0b11111110,
                (byte) 0b11111000,
                (byte) 0b01111100,
                (byte) 0b00111110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter T - (0x54)
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000
        }, {                   // letter U - (0x55)
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter V - (0x56)
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b01100100,
                (byte) 0b01111100,
                (byte) 0b00111000,
                (byte) 0b00000000
        }, {                   // letter W - (0x57)
                (byte) 0b11000110,
                (byte) 0b11000110,
                (byte) 0b11000110,
                (byte) 0b11010110,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b01101100,
                (byte) 0b00000000
        }, {                   // letter X - (0x58)
                (byte) 0b11101110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00111000,
                (byte) 0b01111100,
                (byte) 0b11111110,
                (byte) 0b11101110,
                (byte) 0b00000000
        }, {                   // letter Y - (0x59)
                (byte) 0b11101110,
                (byte) 0b11101110,
                (byte) 0b11111110,
                (byte) 0b01111100,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000
        }, {                   // letter Z - (0x5a)
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b00111100,
                (byte) 0b01111000,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b11111110,
                (byte) 0b00000000
        }, {                   // letter [ - (0x5b)
                (byte) 0b00111100,
                (byte) 0b00110000,
                (byte) 0b00110000,
                (byte) 0b00110000,
                (byte) 0b00110000,
                (byte) 0b00110000,
                (byte) 0b00111100,
                (byte) 0b00000000
        }, {                   // letter \ - (0x5c)
                (byte) 0b00000000,
                (byte) 0b11000000,
                (byte) 0b01100000,
                (byte) 0b00110000,
                (byte) 0b00011000,
                (byte) 0b00001100,
                (byte) 0b00000110,
                (byte) 0b00000000
        }, {                   // letter ] - (0x5d)
                (byte) 0b00111100,
                (byte) 0b00001100,
                (byte) 0b00001100,
                (byte) 0b00001100,
                (byte) 0b00001100,
                (byte) 0b00001100,
                (byte) 0b00111100,
                (byte) 0b00000000
        }, {                   // letter ^ - (0x5e)
                (byte) 0b00011000,
                (byte) 0b00111100,
                (byte) 0b01110110,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter _ - (0x5f)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111110,
                (byte) 0b01111110
        }, {                   // letter ` - (0x60)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b00000000
        }, {                   // letter a - (0x61)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111100,
                (byte) 0b00001100,
                (byte) 0b01111100,
                (byte) 0b11101100,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter b - (0x62)
                (byte) 0b11100000,
                (byte) 0b11100000,
                (byte) 0b11111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111100,
                (byte) 0b00000000
        }, {                   // letter c - (0x63)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111100,
                (byte) 0b11100110,
                (byte) 0b11100000,
                (byte) 0b11100110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter d - (0x64)
                (byte) 0b00001110,
                (byte) 0b00001110,
                (byte) 0b01111110,
                (byte) 0b11001110,
                (byte) 0b11001110,
                (byte) 0b11001110,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter e - (0x65)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111100,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b11100000,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter f - (0x66)
                (byte) 0b00011100,
                (byte) 0b00111010,
                (byte) 0b00111000,
                (byte) 0b11111110,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00000000
        }, {                   // letter g - (0x67)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111110,
                (byte) 0b11001110,
                (byte) 0b11001110,
                (byte) 0b01111110,
                (byte) 0b00001110,
                (byte) 0b01111100
        }, {                   // letter h - (0x68)
                (byte) 0b11100000,
                (byte) 0b11100000,
                (byte) 0b11111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b00000000
        }, {                   // letter i - (0x69)
                (byte) 0b00111000,
                (byte) 0b00000000,
                (byte) 0b01111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter j - (0x6a)
                (byte) 0b00011100,
                (byte) 0b00000000,
                (byte) 0b00011100,
                (byte) 0b00011100,
                (byte) 0b00011100,
                (byte) 0b11011100,
                (byte) 0b11011100,
                (byte) 0b01111000
        }, {                   // letter k - (0x6b)
                (byte) 0b11100000,
                (byte) 0b11100000,
                (byte) 0b11101110,
                (byte) 0b11111100,
                (byte) 0b11111000,
                (byte) 0b11111100,
                (byte) 0b11101110,
                (byte) 0b00000000
        }, {                   // letter l - (0x6c)
                (byte) 0b01111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b00111000,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter m - (0x6d)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11111100,
                (byte) 0b11011010,
                (byte) 0b11011010,
                (byte) 0b11011010,
                (byte) 0b11011010,
                (byte) 0b00000000
        }, {                   // letter n - (0x6e)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b00000000
        }, {                   // letter o - (0x6f)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b01111100,
                (byte) 0b00000000
        }, {                   // letter p - (0x70)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11111100,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111100,
                (byte) 0b11100000,
                (byte) 0b11100000
        }, {                   // letter q - (0x71)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111110,
                (byte) 0b11001110,
                (byte) 0b11001110,
                (byte) 0b01111110,
                (byte) 0b00001110,
                (byte) 0b00001110
        }, {                   // letter r - (0x72)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11101100,
                (byte) 0b11111110,
                (byte) 0b11110110,
                (byte) 0b11100000,
                (byte) 0b11100000,
                (byte) 0b00000000
        }, {                   // letter s - (0x73)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b01111110,
                (byte) 0b11111000,
                (byte) 0b01111100,
                (byte) 0b00111110,
                (byte) 0b11111100,
                (byte) 0b00000000
        }, {                   // letter t - (0x74)
                (byte) 0b00110000,
                (byte) 0b00110000,
                (byte) 0b11111100,
                (byte) 0b00110000,
                (byte) 0b00110000,
                (byte) 0b00110110,
                (byte) 0b00011100,
                (byte) 0b00000000
        }, {                   // letter u - (0x75)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11111110,
                (byte) 0b01111110,
                (byte) 0b00000000
        }, {                   // letter v - (0x76)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b11100110,
                (byte) 0b01111100,
                (byte) 0b00111000,
                (byte) 0b00000000
        }, {                   // letter w - (0x77)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11000110,
                (byte) 0b11010110,
                (byte) 0b11010110,
                (byte) 0b11010110,
                (byte) 0b01101100,
                (byte) 0b00000000
        }, {                   // letter x - (0x78)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11101110,
                (byte) 0b01111100,
                (byte) 0b00111000,
                (byte) 0b01111100,
                (byte) 0b11101110,
                (byte) 0b00000000
        }, {                   // letter y - (0x79)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11001110,
                (byte) 0b11001110,
                (byte) 0b11001110,
                (byte) 0b01111110,
                (byte) 0b00001110,
                (byte) 0b01111100
        }, {                   // letter z - (0x7a)
                (byte) 0b00000000,
                (byte) 0b00000000,
                (byte) 0b11111110,
                (byte) 0b00011100,
                (byte) 0b00111000,
                (byte) 0b01110000,
                (byte) 0b11111110,
                (byte) 0b00000000
        },
    };
}

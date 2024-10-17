package com.example.demo.math;

public class MathOperations {

    public Double sum(String numberOne, String numberTwo) throws Exception {
        return Double.parseDouble(numberOne) + Double.parseDouble(numberTwo);
    }

    public Double sub(String numberOne, String numberTwo) throws Exception {
        return Double.parseDouble(numberOne) - Double.parseDouble(numberTwo);
    }

    public Double mult(String numberOne, String numberTwo) throws Exception {
        return Double.parseDouble(numberOne) * Double.parseDouble(numberTwo);
    }

    public Double div(String numberOne, String numberTwo) throws Exception {
        return Double.parseDouble(numberOne) / Double.parseDouble(numberTwo);
    }

    public Double mean(String numberOne, String numberTwo) throws Exception {
        return (Double.parseDouble(numberOne) + Double.parseDouble(numberTwo)) / 2;
    }

    public Double sqrt(String number) throws Exception {
        return Math.sqrt(Double.parseDouble(number));
    }
}

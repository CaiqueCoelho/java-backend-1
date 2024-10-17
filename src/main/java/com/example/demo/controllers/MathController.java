package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.math.MathOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import static com.example.demo.math.NumberUtils.isNumeric;

@RestController(value = "/api/math")
public class MathController {
    private final AtomicLong counter = new AtomicLong();
    private MathOperations mathOperations = new MathOperations();

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new ResourceNotFoundException("Please set a numeric value");
        }
        return mathOperations.sum(numberOne, numberTwo);
    }

    @GetMapping("/sub/{numberOne}/{numberTwo}")
    public Double sub(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new ResourceNotFoundException("Please set a numeric value");
        }
        return mathOperations.sub(numberOne, numberTwo);
    }

    @GetMapping("/mult/{numberOne}/{numberTwo}")
    public Double mult(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new ResourceNotFoundException("Please set a numeric value");
        }
        return mathOperations.mult(numberOne, numberTwo);
    }

    @GetMapping("/div/{numberOne}/{numberTwo}")
    public Double div(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new ResourceNotFoundException("Please set a numeric value");
        }
        return mathOperations.div(numberOne, numberTwo);
    }

    @GetMapping("/mean/{numberOne}/{numberTwo}")
    public Double mean(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw new ResourceNotFoundException("Please set a numeric value");
        }
        return mathOperations.mean(numberOne, numberTwo);
    }

    @GetMapping("/sqrt/{number}")
    public Double sqrt(@PathVariable(value = "number") String number) throws Exception {
        if (!isNumeric(number)) {
            throw new ResourceNotFoundException("Please set a numeric value");
        }
        return mathOperations.sqrt(number);
    }
}

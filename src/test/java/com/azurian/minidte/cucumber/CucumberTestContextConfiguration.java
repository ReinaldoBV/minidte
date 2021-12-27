package com.azurian.minidte.cucumber;

import com.azurian.minidte.MinidteApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = MinidteApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}

package com.cybertek.library.runners;

import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "html:target/cucumber-report.html",
                "rerun:target/rerun.txt",
                "json:target/report.json"
        },
        features = "src/test/resources/features",
        glue = "com/cybertek/library/step_definitions",
        dryRun = true,
        tags = "@wip"
)
public class CukesRunner {
}

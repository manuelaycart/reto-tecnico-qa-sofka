package petstore;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Ejecuta los features de PetStore y genera el reporte HTML en target/karate-reports.
 * Los features marcados con @ignore son pasos reutilizables y no se ejecutan solos.
 *
 * Ejecutar un caso puntual por tag:
 *   mvn clean test -Dkarate.options="--tags @actualizar"
 */
class PetStoreRunnerTest {

    @Test
    void ejecutarPruebasDeUsuarios() {
        Results resultados = Runner.path("classpath:petstore")
                .tags("~@ignore")
                .outputCucumberJson(true)
                .outputJunitXml(true)
                .parallel(1);

        assertEquals(0, resultados.getFailCount(), resultados.getErrorMessages());
    }
}

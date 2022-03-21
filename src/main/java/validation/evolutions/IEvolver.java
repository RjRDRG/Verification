package validation.evolutions;

import io.swagger.v3.oas.models.OpenAPI;
import validation.result.Result;
import validation.result.Soundness;

public interface IEvolver {
    void process(OpenAPI oldV, OpenAPI newV, Result inOutResult);
}

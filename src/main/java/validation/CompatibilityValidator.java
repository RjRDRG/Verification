package validation;

import io.swagger.v3.oas.models.OpenAPI;
import utils.APIUtils;
import validation.result.Endpoint;
import validation.result.Result;
import validation.evolutions.IEvolver;
import validation.result.Soundness;

import java.util.List;

public class CompatibilityValidator {

    IEvolver[] evolvers;

    public CompatibilityValidator(IEvolver... evolvers) {
        this.evolvers = evolvers;
    }

    public Result process(OpenAPI oldV, OpenAPI newV) {
        Result result = new Result();
        result.setEndpoints(APIUtils.extractEndpoints(newV));

        for (IEvolver evolver : evolvers) {
            evolver.process(oldV, newV, result);
        }

        return result;
    }
}

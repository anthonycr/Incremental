package test_case;

import com.anthonycr.incremental.AutoAggregating;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * A processor annotated with {@link AutoAggregating}.
 */
@AutoAggregating
public class AggregatingExample extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

}

package spoon.ast.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.ast.api.TreeLevel;

import static org.assertj.core.api.Assertions.assertThat;

class TestSpoonTreeCmdBase {
    SpoonTreeCmdBase analyser;

    @BeforeEach
    void setUp() {
        analyser = new SpoonTreeCmdBase(true, "", TreeLevel.CLASS_ELEMENT);
    }

    @Test
    void testBuildEmptyClass() {
        analyser = new SpoonTreeCmdBase(true, "public class Foo {}", TreeLevel.CLASS_ELEMENT);
        final var res = analyser.execute().orElseThrow();
        assertThat(res.getChildren()).hasSize(1);
        assertThat(res.getChildren().get(0).getChildren()).hasSize(0);
        assertThat(res.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: typeMember) : Foo");
    }

    @Test
    void testBuildClassWithTwoAttributes() {
        analyser = new SpoonTreeCmdBase(true, "public class Foo {int foo1; String foo2}", TreeLevel.CLASS_ELEMENT);
        final var res = analyser.execute().orElseThrow().getChildren().get(0).getChildren();
        assertThat(res).hasSize(2);
        assertThat(res.get(0).getLabel()).isEqualTo("CtField (role: typeMember) : foo1");
        assertThat(res.get(1).getLabel()).isEqualTo("CtField (role: typeMember) : foo2");
    }

    @Test
    void testBuildExpressionLevel() {

    }
}

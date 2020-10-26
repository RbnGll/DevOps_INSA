package spoon.ast.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.ast.api.SpoonAST;
import spoon.ast.api.TreeLevel;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtModule;
import spoon.reflect.declaration.CtPackage;

import static org.assertj.core.api.Assertions.assertThat;

class TestSpoonTreeCmdBase {
    SpoonTreeCmdBase analyser;

    @BeforeEach
    void setUp() {
        analyser = new SpoonTreeCmdBase(true, "", TreeLevel.CLASS_ELEMENT);
    }

    @Test
    void testSetCode() {
        analyser.setCode("code");
        final var res = analyser.execute().orElseThrow();
        assertThat(res.getChildren()).hasSize(0);

    }

    // @Test
    // void testBuildEmptyClass() {
    //     analyser = new SpoonTreeCmdBase(true, "public class Foo {}", TreeLevel.AUTO);
    //     final var res = analyser.execute().orElseThrow();
    //     assertThat(res.getChildren()).hasSize(1);
    //     assertThat(res.getChildren().get(0).getChildren()).hasSize(0);
    //     assertThat(res.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: typeMember) : Foo");
    // }

    @Test
    void testBuildClassWithTwoAttributes() {
        analyser = new SpoonTreeCmdBase(true, "public class Foo {int foo1; String foo2}", TreeLevel.CLASS_ELEMENT);
        final var res = analyser.execute().orElseThrow().getChildren().get(0).getChildren();
        assertThat(res).hasSize(2);
        assertThat(res.get(0).getLabel()).isEqualTo("CtField (role: typeMember) : foo1");
        assertThat(res.get(1).getLabel()).isEqualTo("CtField (role: typeMember) : foo2");
    }

    @Test
    void testBuildCodeOk() {
        analyser = new SpoonTreeCmdBase(true, "public class Foo{}", TreeLevel.CLASS_ELEMENT);
        CtModel model = analyser.buildCode("public class Foo{}", 0);
        assertThat(model.isBuildModelFinished());
        assertThat(model.getRootPackage().toString()).isEqualTo(CtPackage.TOP_LEVEL_PACKAGE_NAME);
        assertThat(model.getUnnamedModule().toString()).isEqualTo(CtModule.TOP_LEVEL_MODULE_NAME);

    }

    @Test
    void testBuildExpressionLevelOk() {
        analyser  = new SpoonTreeCmdBase(true, "2", TreeLevel.EXPRESSION);
        final var model = analyser.buildExpressionLevel();
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(model.isBuildModelFinished());
        assertThat(model.getRootPackage().toString()).isEqualTo(CtPackage.TOP_LEVEL_PACKAGE_NAME);
        assertThat(model.getUnnamedModule().toString()).isEqualTo(CtModule.TOP_LEVEL_MODULE_NAME);

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtLiteral (role: expression) : 2");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtTypeReference (role: type) : int");
    }

    // @Test
    // void testBuildStatementLevelOk() {
    //     analyser = new SpoonTreeCmdBase(true, "int a, b; a=1; b=2; return a+b",TreeLevel.STATEMENT);
    //     final var model = analyser.buildStatementLevel();
    //     SpoonAST spoonAST = analyser.execute().orElseThrow();
//
    //     assertThat(model.isBuildModelFinished());
    //     assertThat(model.getRootPackage().toString()).isEqualTo(CtPackage.TOP_LEVEL_PACKAGE_NAME);
    //     assertThat(model.getUnnamedModule().toString()).isEqualTo(CtModule.TOP_LEVEL_MODULE_NAME);
    //     assertThat(spoonAST.getChildren()).hasSize(1);
    //     assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtLocalVariable (role: statement) : a");
    // }

    @Test
    void testBuildClassElementLevelOk() {
        String code = "public int two(){return 2;}\npublic void print(){System.out.println(\"42 is the answer\")} ";
        analyser = new SpoonTreeCmdBase(true, code, TreeLevel.CLASS_ELEMENT);
        final var model = analyser.buildStatementLevel();
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(model.isBuildModelFinished());
        assertThat(model.getRootPackage().toString()).isEqualTo(CtPackage.TOP_LEVEL_PACKAGE_NAME);
        assertThat(model.getUnnamedModule().toString()).isEqualTo(CtModule.TOP_LEVEL_MODULE_NAME);
        System.out.println(spoonAST);
    }

    @Test
    void testBuildClassLevel() {
        analyser = new SpoonTreeCmdBase(true, "public class Test{public int foo; Test(){super()}; Test(int val){this.foo=val;}}", TreeLevel.AUTO);
        final var model = analyser.buildClassLevel();

    }
}

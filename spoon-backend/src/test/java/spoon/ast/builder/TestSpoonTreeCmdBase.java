package spoon.ast.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.ast.api.SpoonAST;
import spoon.ast.api.TreeLevel;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtModule;
import spoon.reflect.declaration.CtPackage;

import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestSpoonTreeCmdBase {
    SpoonTreeCmdBase analyser;

    @BeforeEach
    void setUp() {
        analyser = new SpoonTreeCmdBase(true, "", TreeLevel.CLASS_ELEMENT);
    }

    @Test
    void testSetNullCode() {
        assertThrows(IllegalArgumentException.class, () -> analyser.setCode(null));
        //analyser.setCode(null);
    }

    @Test
    void testSetCode() {
        // Do it using a stub
        class SpoonTreeCmdBaseWithCodeGetter extends SpoonTreeCmdBase {

            private String code;

            public SpoonTreeCmdBaseWithCodeGetter(boolean hideImplicit, @NotNull String code, @NotNull TreeLevel treeLevel) {
                super(hideImplicit, code, treeLevel);
                this.code = code;
            }

            public String getCode() {
                return this.code;
            }

            @Override
            public void setCode(@NotNull String code) {
                super.setCode(code);
                this.code = code;
            }
        }
        SpoonTreeCmdBaseWithCodeGetter analyser = new SpoonTreeCmdBaseWithCodeGetter(true, "", TreeLevel.CLASS_ELEMENT);
        analyser.setCode("code");
        assertThat(analyser.getCode()).isEqualTo("code");
    }

    @Test
    void testBuildEmptyClass() {
        analyser = new SpoonTreeCmdBase(true, "public class Foo {}", TreeLevel.AUTO);
        final var res = analyser.execute().orElseThrow();

        assertThat(res.getChildren()).hasSize(1);
        assertThat(res.getChildren().get(0).getChildren()).hasSize(0);
        // assertThat(res.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: typeMember) : Foo");
        assertThat(res.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: containedType) : Foo");
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
    void testBuildCodeOk() {
        analyser = new SpoonTreeCmdBase(true, "public class Foo{}", TreeLevel.CLASS_ELEMENT);
        @SuppressWarnings("CheckReturnValue")
        CtModel model = analyser.buildCode("public class Foo{}", 0);

        assertThat(model.isBuildModelFinished()).isTrue();
        assertThat(model.getRootPackage().toString()).isEqualTo(CtPackage.TOP_LEVEL_PACKAGE_NAME);
        assertThat(model.getUnnamedModule().toString()).isEqualTo(CtModule.TOP_LEVEL_MODULE_NAME);
    }

    @Test
    void testBuildExpressionLevelOk() {
        analyser  = new SpoonTreeCmdBase(true, "2", TreeLevel.EXPRESSION);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtLiteral (role: expression) : 2");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtTypeReference (role: type) : int");
    }

    @Test
    void testBuildReturnStatement() {
        analyser = new SpoonTreeCmdBase(true, "return", TreeLevel.STATEMENT);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtReturn (role: statement)");

    }

    @Test
    void testBuildIfStatement() {
        analyser = new SpoonTreeCmdBase(true, "if(true){int tmp = 1; int res=2;}", TreeLevel.STATEMENT);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtIf (role: statement)");
        assertThat(spoonAST.getChildren().get(0).getChildren()).hasSize(2);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtLiteral (role: condition) : true");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getLabel()).isEqualTo("CtBlock (role: then)");
    }

    @Test
    void testBuildClassWithTwoMethods() {
        String code = "public int two(){return 2;} public void print(){System.out.println(\"42 is the answer\")}";
        analyser = new SpoonTreeCmdBase(true, code, TreeLevel.CLASS_ELEMENT);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(2);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtMethod (role: typeMember) : two");
        assertThat(spoonAST.getChildren().get(0).getChildren()).hasSize(2);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getLabel()).isEqualTo("CtBlock (role: body)");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getChildren().get(0).getLabel()).isEqualTo("CtReturn (role: statement)");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getChildren().get(0).getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtLiteral (role: expression) : 2");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getChildren().get(0).getChildren().get(0).getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getChildren().get(0).getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtTypeReference (role: type) : int");


        assertThat(spoonAST.getChildren().get(1).getLabel()).isEqualTo("CtMethod (role: typeMember) : print");
        assertThat(spoonAST.getChildren().get(1).getChildren()).hasSize(2);
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getLabel()).isEqualTo("CtBlock (role: body)");
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getChildren().get(0).getLabel()).isEqualTo("CtInvocation (role: statement) : println");
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getChildren().get(0).getChildren()).hasSize(3);
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtFieldRead (role: target) : out");
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getChildren().get(0).getChildren().get(1).getLabel()).isEqualTo("CtExecutableReference (role: executableRef) : println");
        assertThat(spoonAST.getChildren().get(1).getChildren().get(1).getChildren().get(0).getChildren().get(2).getLabel()).isEqualTo("CtLiteral (role: argument) : 42 is the answer");

    }

    @Test
    void testBuildEmptyClassLevel() {
        // analyser = new SpoonTreeCmdBase(true, "public class Test{public int foo; Test(){super()}; Test(int val){this.foo=val;}}", TreeLevel.AUTO);
        analyser = new SpoonTreeCmdBase(true, "public class Test{}", TreeLevel.AUTO);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: containedType) : Test");
    }

    @Test
    void testBuildClassWithOneAttributeAndTwoConstructors() {
        analyser = new SpoonTreeCmdBase(true, "public class SimpleClass{ private String value; public SimpleClass(){super();} public SimpleClass(final String val){this.value = val;} }", TreeLevel.AUTO);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: containedType) : SimpleClass");
        assertThat(spoonAST.getChildren().get(0).getChildren()).hasSize(3);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtField (role: typeMember) : value");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(1).getLabel()).isEqualTo("CtConstructor (role: typeMember) : <init>");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(2).getLabel()).isEqualTo("CtConstructor (role: typeMember) : <init>");
    }

    @Test
    void testBuildEmptyCode() {
        analyser = new SpoonTreeCmdBase(true, "", TreeLevel.AUTO);
        SpoonAST spoonAST = analyser.execute().orElseThrow();
        assertThat(spoonAST.getChildren()).hasSize(0);
    }

    @Test
    void testBuildClassWithOneAnnotatedAttribute() {
        analyser = new SpoonTreeCmdBase(true, "public class SimpleClass{ @NotNull private String value;}", TreeLevel.AUTO);
        SpoonAST spoonAST = analyser.execute().orElseThrow();

        assertThat(spoonAST.getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getLabel()).isEqualTo("CtClass (role: containedType) : SimpleClass");
        assertThat(spoonAST.getChildren().get(0).getChildren()).hasSize(1);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtField (role: typeMember) : value");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getChildren().get(0).getLabel()).isEqualTo("CtAnnotation (role: annotation) : NotNull");
        assertThat(spoonAST.getChildren().get(0).getChildren().get(0).getChildren().get(1).getLabel()).isEqualTo("CtTypeReference (role: type) : String");
    }
}

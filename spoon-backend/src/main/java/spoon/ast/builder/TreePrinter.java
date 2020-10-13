/*
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package spoon.ast.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spoon.ast.api.SpoonAST;
import spoon.ast.impl.SpoonASTImpl;
import spoon.reflect.declaration.CtElement;

import java.util.List;
import java.util.Optional;

/**
 * The printer that prints the Spoon AST into a JavaFX tree view.
 */
public class TreePrinter extends SpoonElementVisitor {
    private @Nullable SpoonAST tree;
    private @Nullable SpoonAST currItem;
    /**
     * The current depth level in the tree view
     */
    private int currLevel;

    /**
     * @param tree           The tree view to use to print the Spoon AST
     * @param levelsToIgnore The number of tree levels to ignore before starting printing
     */
    public TreePrinter(final int levelsToIgnore) {
        super(levelsToIgnore);
        tree = null;
        currItem = null;
    }

    /**
     * Create an initial text flow for naming the nodes of the Spoon AST.
     *
     * @param label         The Spoon element to analyse.
     * @param startPosition
     * @param endPosition
     * @return The created text flow that can be completed with other text elements.
     */
    SpoonAST createStdTextFlow(final TreeNodeLabel label, final int startPosition, final int endPosition) {
        final String url = "http://spoon.gforge.inria.fr/mvnsites/spoon-core/apidocs/"
                + label.fullName.replace('.', '/') + ".html";

        return new SpoonASTImpl(label.toString(), url, startPosition, endPosition);
    }

    @Override
    public void visitElement(final CtElement elt, final int level, final @NotNull TreeNodeLabel label, final @NotNull List<Integer> lines) {
        // level > 1 because the root element must be created to be then masked as several real tree roots may exist
        // Example: three statements with the statement level.
        // level <= levelsToIgnore: depending on the analysis level, some root elements must be hidden
        if (level > 1 && level <= levelsToIgnore) {
            return;
        }

        final int startPosition = lines.isEmpty() ? -1 : lines.get(0);
        final int endPosition = lines.isEmpty() ? -1 : lines.get(1);
        final SpoonAST item = createStdTextFlow(label, startPosition, endPosition);

        if (currItem == null) {
            tree = item;
        } else {
            if (currLevel < level) {
                currItem.addChild(item);
            } else {
                var parent = currItem.getParent();

                while (currLevel > level && parent.isPresent()) {
                    currLevel--;
                    parent = parent.get().getParent();
                }

                parent.ifPresent(p -> p.addChild(item));
            }
        }

        currLevel = level;
        currItem = item;
    }

    public Optional<SpoonAST> getTree() {
        return Optional.ofNullable(tree);
    }
}

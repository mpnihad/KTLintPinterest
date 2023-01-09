package com.nihad.custom_rules

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.*
import com.pinterest.ktlint.core.ast.ElementType.ANNOTATION
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

public class IgnoreFileRule : Rule("$CUSTOM_RULE_SET_ID:add-to-ignore-file") {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {


        //Only check if the file is visited for first time
        if (node.isRoot()) {
            val ignoredFileNames = setOf<Pair<String, Boolean>>(
                Pair("MainActivity.kt", true)
            )
            val fileName =
                ((node.psi as? KtFile)?.name)?.substringAfterLast("/") // get the file name

            val text = node.text
            var line: List<String> = text.split("\n")

            //checks if the file comes under the ignore file list
            if (ignoredFileNames.find { it.first == fileName } != null) {

                if (isFileAlreadyIgnored(line)) {
                    //get the first node and append the file suppress code at the beginning
                    node
                        .firstChildLeafOrSelf().safeAs<LeafPsiElement>()?.let {
                            val newNode =
                                (LeafPsiElement(ANNOTATION, "@file:Suppress(\"ktlint\")\n"))
                            it.rawInsertBeforeMe(newNode)
                        }


                }
                emit(node.startOffset, "Added File suppress", true)
            } else {
                return
            }
        }


    }


    private fun isFileAlreadyIgnored(line: List<String>) =
        !(line[0].trim().startsWith("@file:Suppress(\"ktlint\")"))
}



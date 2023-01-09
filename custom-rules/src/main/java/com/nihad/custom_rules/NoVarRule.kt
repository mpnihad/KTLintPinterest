package com.nihad.custom_rules

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.isRoot
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtFile

public class NoVarRule : Rule("$CUSTOM_RULE_SET_ID:add-to-ignore-file") {
    override fun beforeVisitChildNodes(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit,
    ) {

        val ignoredFileNames = setOf<Pair<String, Boolean>>(
            Pair("MainActivity.kt", true)
        )
        if (node.isRoot()) {

            val fileName =
                ((node.psi as? KtFile)?.name)?.substringAfterLast("/") // get the file name
            println(fileName)
            val text = node.text
            var line: List<String> = text.split("\n")

            if (ignoredFileNames.find { it.first == fileName } != null) {
                println(line[0])
                if (!(line[0].trim().startsWith("@file:Suppress(\"ktlint\")"))) {
                    // TODO: Add Option insert the text "@file:Suppress(\"ktlint\")"

                    println("Succesfull")
                }
                emit(node.startOffset, "Added File suppress", false)
            } else {
                return
            }
        }


    }
}



/*
 * Copyright (c) 2017. tangzx(love.tangzx@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tang.intellij.lua.unity.ty

import com.intellij.psi.PsiNamedElement
import com.tang.intellij.lua.ext.ILuaTypeInfer
import com.tang.intellij.lua.psi.*
import com.tang.intellij.lua.search.SearchContext
import com.tang.intellij.lua.ty.ITy
import com.tang.intellij.lua.ty.Ty
import com.tang.intellij.lua.ty.TySerializedClass

class UnityTypeInfer : ILuaTypeInfer {
    override fun inferType(target: LuaTypeGuessable, context: SearchContext): ITy {
        return when (target) {
            is LuaCallExpr -> {
                val name = (target.expr as? PsiNamedElement)?.name
                if (name == "GetComponent") {
                    val arg = target.argList.firstOrNull()
                    if (arg is LuaLiteralExpr) {
                        return TySerializedClass("UnityEngine.${arg.stringValue}")
                    }
                }
                Ty.UNKNOWN
            }
            else -> Ty.UNKNOWN
        }
    }
}
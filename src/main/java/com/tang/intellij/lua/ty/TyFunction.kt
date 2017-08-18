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

package com.tang.intellij.lua.ty

import com.tang.intellij.lua.comment.psi.LuaDocFunctionTy
import com.tang.intellij.lua.comment.psi.resolveDocTypeSet
import com.tang.intellij.lua.psi.LuaFuncBodyOwner
import com.tang.intellij.lua.psi.LuaParamInfo
import com.tang.intellij.lua.search.SearchContext


abstract class TyFunction : Ty(TyKind.Function) {
    abstract val returnTy: Ty
    abstract val params: Array<LuaParamInfo>

    override val displayName: String
        get() = "function"
}

class TyPsiFunction(val psi: LuaFuncBodyOwner, searchContext: SearchContext) : TyFunction() {
    private val _retTy: Ty = psi.guessReturnTypeSet(searchContext)

    override val returnTy: Ty
        get() = _retTy
    override val params: Array<LuaParamInfo>
        get() = psi.params
}

class TyDocPsiFunction(func: LuaDocFunctionTy, searchContext: SearchContext) : TyFunction() {
    private val _retTy: Ty = func.getReturnType(searchContext)

    private fun initParams(func: LuaDocFunctionTy, searchContext: SearchContext): Array<LuaParamInfo> {
        val list = mutableListOf<LuaParamInfo>()
        func.functionParamList.forEach {
            val p = LuaParamInfo()
            p.name = it.id.text
            p.ty = resolveDocTypeSet(it.typeSet, searchContext)
            list.add(p)
        }
        return list.toTypedArray()
    }

    private val _params = initParams(func, searchContext)

    override val returnTy: Ty
        get() = _retTy
    override val params: Array<LuaParamInfo>
        get() = _params
}

class TySerializedFunction(override val returnTy: Ty, override val params: Array<LuaParamInfo>) : TyFunction()
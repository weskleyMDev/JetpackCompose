package com.weskley.hdc_app.model

import javax.inject.Inject

class InputModel @Inject constructor() {
    private var _descricao: String = ""
    var descricao: String
        get() = _descricao
        set(value) {
            _descricao = value
        }

    private var _titulo: String = ""
    var titulo: String
        get() = _titulo
        set(value) {
            _titulo = value
        }
}
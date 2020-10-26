package com.example.expenseappmvvm.utils.enums

import android.content.Context
import com.example.expenseappmvvm.R

enum class CurrencyEnum {
    RON {
        override fun getName(context: Context): String {
            return RON.name
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_flag_ro
        }
    },
    EUR {
        override fun getName(context: Context): String {
            return EUR.name
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_euro
        }
    },
    USD {
        override fun getName(context: Context): String {
            return USD.name
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_dolar
        }
    },
    GBP {
        override fun getName(context: Context): String {
            return GBP.name
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_pound
        }
    },
    CHF {
        override fun getName(context: Context): String {
            return CHF.name
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_chf
        }
    },
    AUD {
        override fun getName(context: Context): String {
            return AUD.name
        }

        override fun getIcon(context: Context): Int {
            return R.drawable.ic_aud
        }
    };
    abstract fun getName(context: Context): String
    abstract fun getIcon(context: Context): Int
}

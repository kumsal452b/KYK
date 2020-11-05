package com.kumsal.kyk.bottomTabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {

       return when(position){
            0->{
                val home:home_fragment= home_fragment()
                return home;
            }
            1->{
                val message:message_fragment= message_fragment()
                return message
            }
            2->{
                val searchf:search_fragment= search_fragment()
                return searchf
            }
            3->{
                val account:account_fragment=account_fragment()
                return account
            }

           else -> {
               val account:account_fragment=account_fragment()
               return account
           }
       }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0->{

                return "A.Sayfa";
            }
            1->{

                return "Msjlar"
            }
            2->{

                return "Arama"
            }
            3->{
                val account:account_fragment=account_fragment()
                return "Hesap"
            }
            else -> {
                return " "
            }
        }
    }

}
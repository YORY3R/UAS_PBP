package com.yory3r.e_learning.utils;

public class ChangeString
{
    public String DotsToEtc(String string)
    {
        return string.replace('.','~');
    }

    public String EtctoDots(String string)
    {
        return string.replace('~','.');
    }
}

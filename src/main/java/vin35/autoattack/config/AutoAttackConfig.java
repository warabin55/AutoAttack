package vin35.autoattack.config;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfig.Comment;
import eu.midnightdust.lib.config.MidnightConfig.Entry;

public class AutoAttackConfig extends MidnightConfig {
    @Comment
    public static Comment text1;
    @Entry
    public static boolean checkUpdate = true;
    @Entry
    public static boolean preventsHittingBlocks = false;
    @Entry
    public static boolean preventsHittingBlocksSwords = true;
    @Entry
    public  static  boolean cleanCut = true;
    @Entry
    public  static  boolean autoBow = true;
    @Entry
    public  static  boolean autoCrossBow = true;
    @Entry
    public  static  boolean autoTrident = true;
    @Entry
    public  static  boolean afkAttack = false;
}

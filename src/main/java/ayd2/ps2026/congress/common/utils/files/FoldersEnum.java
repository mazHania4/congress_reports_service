package ayd2.ps2026.congress.common.utils.files;

public enum FoldersEnum {

    ROOT("/")
    ;

    private final String name;

    private FoldersEnum(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}

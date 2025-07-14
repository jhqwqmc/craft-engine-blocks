package cn.gtemc.craftEngineBlocks.dependency.relocation;

public record Relocation(String pattern, String relocatedPattern) {
    private static final String RELOCATION_PREFIX = "cn.gtemc.libraries.";

    public static Relocation of(String id, String pattern) {
        return new Relocation(pattern.replace("{}", "."), RELOCATION_PREFIX + id);
    }
}

package io.github.jason13official.happy_ghasts_boost.impl.common;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.happy_ghasts_boost.Constants;
import io.github.jason13official.happy_ghasts_boost.platform.Services;
import io.github.jason13official.monolib.api.common.config.ConfigGetterSetter.Commented;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class EarlyLoadConfig implements IMixinConfigPlugin {

  private static final String PREFIX = "[" + Constants.MOD_NAME + "] ";

  private static int secondsUntilBoost = 4;
  public static Commented<Integer> SECONDS_UNTIL_BOOST = new Commented<>("seconds_until_boost", () -> secondsUntilBoost, value -> secondsUntilBoost = value, "Time flying forward until boost.");

  private static float forwardMultiplier = 2.0f;
  public static Commented<Float> FORWARD_MULTIPLIER = new Commented<>("forward_multiplier", () -> forwardMultiplier, value -> forwardMultiplier = value, "Speed boost from forward movement.");

  private static float potionMultiplier = 2.5f;
  public static Commented<Float> POTION_MULTIPLIER = new Commented<>("potion_multiplier", () -> potionMultiplier, value -> potionMultiplier = value, "Acceleration from Speed potion.");
  private static float potionMultiplierTwo = 3.5f;
  public static Commented<Float> POTION_MULTIPLIER_TWO = new Commented<>("potion_multiplier_two", () -> potionMultiplierTwo, value -> potionMultiplierTwo = value, "Acceleration from Speed II potion.");

  private static boolean displayBoostBar = true;
  public static Commented<Boolean> DISPLAY_BOOST_BAR = new Commented<>("display_boost_bar", () -> displayBoostBar, value -> displayBoostBar = value, "Whether to display the boost bar.");

  @Override
  public void onLoad(String mixinPackage) {

    EarlyLoadConfig.createOrLoadConfiguration();
  }

  @Override
  public String getRefMapperConfig() {
    return null;
  }

  @Override
  public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    return true;
  }

  @Override
  public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

  }

  @Override
  public List<String> getMixins() {
    return List.of();
  }

  @Override
  public void postApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }

  @Override
  public void preApply(String targetClassName, org.objectweb.asm.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

  }

  private static float getFloatOrElse(UnmodifiableConfig config, String key, float defaultValue) {

    Number value = config.get(key);
    return value == null ? defaultValue : value.floatValue();
  }

  public static void createOrLoadConfiguration() {

    Path configDir = Services.PLATFORM.getConfigDirectory();
    File configDirectory = new File(configDir.toUri());

    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {

      System.out.println(PREFIX + "Failed to get or create config directory. Retaining default values.");
      return;
    }

    Path serverConfigFilepath = Services.PLATFORM.getConfigDirectory().resolve(Constants.MOD_ID + "-server.toml");
    File serverConfigFile = new File(serverConfigFilepath.toUri());

    try (CommentedFileConfig serverConfig = CommentedFileConfig.builder(serverConfigFile).build()) {

      if (Files.exists(serverConfigFilepath)) {
        serverConfig.load();
      }

      // getters (from serverConfig)
      SECONDS_UNTIL_BOOST.set(serverConfig.getIntOrElse(SECONDS_UNTIL_BOOST.key(), SECONDS_UNTIL_BOOST.get()));

      FORWARD_MULTIPLIER.set(getFloatOrElse(serverConfig, FORWARD_MULTIPLIER.key(), FORWARD_MULTIPLIER.get()));
      POTION_MULTIPLIER.set(getFloatOrElse(serverConfig, POTION_MULTIPLIER.key(), POTION_MULTIPLIER.get()));
      POTION_MULTIPLIER_TWO.set(getFloatOrElse(serverConfig, POTION_MULTIPLIER_TWO.key(), POTION_MULTIPLIER_TWO.get()));

      // setters (to serverConfig)
      serverConfig.setComment(SECONDS_UNTIL_BOOST.key(), SECONDS_UNTIL_BOOST.comment());
      serverConfig.set(SECONDS_UNTIL_BOOST.key(), SECONDS_UNTIL_BOOST.get());

      serverConfig.setComment(FORWARD_MULTIPLIER.key(), FORWARD_MULTIPLIER.comment());
      serverConfig.set(FORWARD_MULTIPLIER.key(), FORWARD_MULTIPLIER.get());
      serverConfig.setComment(POTION_MULTIPLIER.key(), POTION_MULTIPLIER.comment());
      serverConfig.set(POTION_MULTIPLIER.key(), POTION_MULTIPLIER.get());
      serverConfig.setComment(POTION_MULTIPLIER_TWO.key(), POTION_MULTIPLIER_TWO.comment());
      serverConfig.set(POTION_MULTIPLIER_TWO.key(), POTION_MULTIPLIER_TWO.get());

      serverConfig.save();
    } catch (Exception e) {
      System.out.println(PREFIX + "Failed to get or create server config: " + e.getMessage());
      e.printStackTrace();
    }

    if (!Services.PLATFORM.isClientSide()) {
      return;
    }

    Path clientConfigFilepath = Services.PLATFORM.getConfigDirectory().resolve(Constants.MOD_ID + "-client.toml");
    File clientConfigFile = new File(clientConfigFilepath.toUri());

    try (CommentedFileConfig clientConfig = CommentedFileConfig.builder(clientConfigFile).build()) {

      if (Files.exists(clientConfigFilepath)) {
        clientConfig.load();
      }

      // getters (from clientConfig)
      DISPLAY_BOOST_BAR.set(clientConfig.getOrElse(DISPLAY_BOOST_BAR.key(), DISPLAY_BOOST_BAR.get()));

      // setters (to clientConfig)
      clientConfig.setComment(DISPLAY_BOOST_BAR.key(), DISPLAY_BOOST_BAR.comment());
      clientConfig.set(DISPLAY_BOOST_BAR.key(), DISPLAY_BOOST_BAR.get());

      clientConfig.save();
    } catch (Exception e) {
      System.out.println(PREFIX + "Failed to get or create client config: " + e.getMessage());
      e.printStackTrace();
    }
  }
}

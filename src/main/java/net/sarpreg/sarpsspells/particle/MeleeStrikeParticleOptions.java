package net.sarpreg.sarpsspells.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.sarpreg.sarpsspells.registries.SarparticleRegistry;
import org.jetbrains.annotations.NotNull;

public class MeleeStrikeParticleOptions implements ParticleOptions {
    public final float scale;
    public final float xf;
    public final float yf;
    public final float zf;
    public final boolean mirror, vertical;

    public MeleeStrikeParticleOptions(float xf, float yf, float zf, boolean mirror, boolean vertical, float scale) {
        this.scale = scale;
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
        this.mirror = mirror;
        this.vertical = vertical;
    }

//    public static StreamCodec<? super ByteBuf, FlameStrikeParticleOptions> STREAM_CODEC = StreamCodec.of(
//            (buf, option) -> {
//                buf.writeFloat(option.xf);
//                buf.writeFloat(option.yf);
//                buf.writeFloat(option.zf);
//                buf.writeBoolean(option.mirror);
//                buf.writeBoolean(option.vertical);
//                buf.writeFloat(option.scale);
//            },
//            (buf) -> new FlameStrikeParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readFloat())
//    );

    public static Codec<MeleeStrikeParticleOptions> CODEC = RecordCodecBuilder.create(object ->
            object.group(
                    Codec.FLOAT.fieldOf("xf").forGetter(p -> ((MeleeStrikeParticleOptions) p).xf),
                    Codec.FLOAT.fieldOf("yf").forGetter(p -> ((MeleeStrikeParticleOptions) p).yf),
                    Codec.FLOAT.fieldOf("zf").forGetter(p -> ((MeleeStrikeParticleOptions) p).zf),
                    Codec.BOOL.fieldOf("mirror").forGetter(p -> ((MeleeStrikeParticleOptions) p).mirror),
                    Codec.BOOL.fieldOf("vertical").forGetter(p -> ((MeleeStrikeParticleOptions) p).vertical),
                    Codec.FLOAT.fieldOf("scale").forGetter(p -> ((MeleeStrikeParticleOptions) p).scale)
            ).apply(object, MeleeStrikeParticleOptions::new
            ));

    public @NotNull ParticleType<MeleeStrikeParticleOptions> getType() {
        return SarparticleRegistry.MELEE_STRIKE_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        var option = this;
        buf.writeFloat(option.xf);
        buf.writeFloat(option.yf);
        buf.writeFloat(option.zf);
        buf.writeBoolean(option.mirror);
        buf.writeBoolean(option.vertical);
        buf.writeFloat(option.scale);
    }

    @Override
    public String writeToString() {
        return "";
    }

    @SuppressWarnings("deprecation")
    public static final Deserializer<MeleeStrikeParticleOptions> DESERIALIZER = new Deserializer<MeleeStrikeParticleOptions>() {
        public @NotNull MeleeStrikeParticleOptions fromCommand(@NotNull ParticleType<MeleeStrikeParticleOptions> p_123689_, @NotNull StringReader p_123690_) throws CommandSyntaxException {
            return new MeleeStrikeParticleOptions(p_123690_.readFloat(), p_123690_.readFloat(), p_123690_.readFloat(), p_123690_.readBoolean(), p_123690_.readBoolean(), p_123690_.readFloat());
        }

        public @NotNull MeleeStrikeParticleOptions fromNetwork(@NotNull ParticleType<MeleeStrikeParticleOptions> p_123692_, @NotNull FriendlyByteBuf buf) {
            return new MeleeStrikeParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readBoolean(), buf.readFloat());
        }
    };
}

package li.cli.oc.render.block

import li.cli.oc.OpenComputers
import li.cli.oc.blockentity.Screen
import li.cli.oc.blocks.commons.BakedModelConfig
import li.cli.oc.blocks.commons.States
import li.cli.oc.render.block.rotations.ScreenRotations
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.math.Vector4f
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockRenderView
import java.util.*
import java.util.function.Supplier

object Flags {
    const val UP = 1 shl 0
    const val DOWN = 1 shl 1
    const val LEFT = 1 shl 2
    const val RIGHT = 1 shl 3

    fun from(up: Boolean, down: Boolean, left: Boolean, right: Boolean): Int {
        return (if (up) UP else 0) or (if (down) DOWN else 0) or (if (left) LEFT else 0) or if (right) RIGHT else 0
    }
}
class ScreenModel : BakedModelConfig() {

    override val spriteIds = arrayOf(
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/b")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bbl")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bbm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bbr")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bhb")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bhm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bht")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bml")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bmm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bmr")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/btl")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/btm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/btr")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bvb")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bvm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/bvt")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/f")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fbl")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fbm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fbr")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fhb")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fhm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fht")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fml")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fmm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fmr")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/ftl")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/ftm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/ftr")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fvb")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fvm")),
        SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, Identifier(OpenComputers.modId, "block/screen/fvt"))
    )
    override val sprites: Array<Sprite?> = arrayOfNulls(spriteIds.size)
    private val srot = ScreenRotations(sprites)


    override fun emitBlockQuads(
        blockRenderView: BlockRenderView?,
        blockState: BlockState?,
        blockPos: BlockPos?,
        supplier: Supplier<Random>?,
        renderContext: RenderContext?
    ) {
        val emitter = renderContext?.emitter!!
        val rotation = blockState?.get(States.Yaw)!!
        val world = MinecraftClient.getInstance().world!!
        val entity = world.getBlockEntity(blockPos)!! as Screen
        val color = entity.getColor()
        val defaultVector = Vector4f(0.0f, 0.0f, 1.0f, 1.0f)
        val connectAt = entity.connectedAt;

        when(blockState.get(States.Pitch)) {
            Direction.UP -> {
                val override = when(rotation) {
                    Direction.EAST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90 or MutableQuadView.BAKE_FLIP_U
                    Direction.WEST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90 or MutableQuadView.BAKE_FLIP_V
                    Direction.SOUTH ->  MutableQuadView.BAKE_LOCK_UV
                    Direction.NORTH ->  MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_180
                    else ->             MutableQuadView.BAKE_LOCK_UV
                }
                val override2 = when(rotation) {
                    Direction.EAST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90
                    Direction.WEST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_270 or MutableQuadView.BAKE_FLIP_V
                    Direction.SOUTH ->  MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_FLIP_V
                    Direction.NORTH ->  MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_180  or MutableQuadView.BAKE_FLIP_V
                    else ->             MutableQuadView.BAKE_LOCK_UV
                }

                arrayOf(Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST).forEach { direction ->
                    renderSprite(
                        emitter, direction,
                        srot.getHorizontalFaceTexture(true,connectAt, rotation, direction),
                        defaultVector,
                        MutableQuadView.BAKE_LOCK_UV,
                        color
                    )
                }
                renderSprite(emitter, Direction.DOWN,   srot.getFrontTexture(connectAt), defaultVector, override2, color)
                renderSprite(emitter, Direction.UP,     srot.getFrontTexture(connectAt, true), defaultVector, override, color)
            }
            Direction.NORTH -> {
                val override = when(rotation) {
                    Direction.EAST -> MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90 or MutableQuadView.BAKE_FLIP_V
                    Direction.WEST -> MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_270
                    Direction.SOUTH -> MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_180
                    Direction.NORTH -> MutableQuadView.BAKE_LOCK_UV
                    else -> MutableQuadView.BAKE_LOCK_UV
                }
                renderSprite(emitter, Direction.UP,                         srot.getVerticalHTexture(connectAt),            defaultVector, override, color)
                renderSprite(emitter, Direction.DOWN,                       srot.getVerticalHTexture(connectAt),            defaultVector, override or if(rotation == Direction.WEST || rotation == Direction.EAST) MutableQuadView.BAKE_FLIP_U else MutableQuadView.BAKE_FLIP_V, color)
                renderSprite(emitter, rotation.rotateYClockwise(),          srot.getVerticalSideTexture(connectAt),         defaultVector, MutableQuadView.BAKE_LOCK_UV, color)
                renderSprite(emitter, rotation.rotateYCounterclockwise(),   srot.getVerticalSideTexture(connectAt),         defaultVector, MutableQuadView.BAKE_LOCK_UV, color)
                renderSprite(emitter, rotation.opposite,                    srot.getFrontTexture(connectAt),                defaultVector, MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_FLIP_U, color)
                renderSprite(emitter, rotation,                             srot.getFrontTexture(connectAt, true),   defaultVector, MutableQuadView.BAKE_LOCK_UV, color)
            }
            Direction.DOWN -> {
                val override = when(rotation) {
                    Direction.EAST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90 or MutableQuadView.BAKE_FLIP_V
                    Direction.WEST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90 or MutableQuadView.BAKE_FLIP_U
                    Direction.SOUTH ->  MutableQuadView.BAKE_LOCK_UV
                    Direction.NORTH ->  MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_180
                    else ->             MutableQuadView.BAKE_LOCK_UV
                }
                val override2 = when(rotation) {
                    Direction.EAST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_90 or MutableQuadView.BAKE_FLIP_V or MutableQuadView.BAKE_FLIP_U
                    Direction.WEST ->   MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_270 or MutableQuadView.BAKE_FLIP_U
                    Direction.SOUTH ->  MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_FLIP_V
                    Direction.NORTH ->  MutableQuadView.BAKE_LOCK_UV or MutableQuadView.BAKE_ROTATE_180  or MutableQuadView.BAKE_FLIP_V
                    else ->             MutableQuadView.BAKE_LOCK_UV
                }
                arrayOf(Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST).forEach { direction ->
                    renderSprite(
                        emitter, direction,
                        srot.getHorizontalFaceTexture(false, connectAt, rotation, direction),
                        defaultVector,
                        MutableQuadView.BAKE_LOCK_UV,
                        color
                    )
                }
                renderSprite(emitter, Direction.DOWN, srot.getFrontTexture(connectAt, true), defaultVector, override, color)
                renderSprite(emitter, Direction.UP, srot.getFrontTexture(connectAt), defaultVector, override2, color)
            }
            else -> renderSprite(emitter, Direction.UP, sprites[2], defaultVector)
        }
   }
    override fun emitItemQuads(p0: ItemStack?, p1: Supplier<Random>?, p2: RenderContext?) { }
}
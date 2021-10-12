package wanion.avaritiaddons.block.chest;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RendererAvaritiaddonsChest extends TileEntitySpecialRenderer<TileEntityAvaritiaddonsChest>
{
    public static final RendererAvaritiaddonsChest INSTANCE = new RendererAvaritiaddonsChest();
    private final ModelChest simpleChest = new ModelChest();

    private RendererAvaritiaddonsChest() {}

    public void render(TileEntityAvaritiaddonsChest tileEntityAvaritiaddonsChest, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i = 3;

        if (tileEntityAvaritiaddonsChest.hasWorld())
            i = tileEntityAvaritiaddonsChest.getBlockMetadata() & 3;

        ModelChest modelchest = this.simpleChest;

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else this.bindTexture(tileEntityAvaritiaddonsChest.getTexture());

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0)
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int j = 0;

        if (i == 0)
            j = 90;
        else if (i == 1)
            j = 180;
        else if (i == 2)
            j = 270;

        GlStateManager.rotate((float) j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = tileEntityAvaritiaddonsChest.prevLidAngle + (tileEntityAvaritiaddonsChest.lidAngle - tileEntityAvaritiaddonsChest.prevLidAngle) * partialTicks;

        f = 1.0F - f;
        f = 1.0F - f * f * f;
        modelchest.chestLid.rotateAngleX = -(f * ((float) Math.PI / 2F));
        modelchest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
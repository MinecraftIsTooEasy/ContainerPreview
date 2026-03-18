package com.github.FlyBird.containerpreview;

import com.github.FlyBird.containerpreview.compat.Backpack;
import com.github.FlyBird.containerpreview.network.C2SGetInventory;
import com.github.FlyBird.containerpreview.network.C2SInform;
import com.github.flybird.backpack.block.BlockBackpack;
import com.github.flybird.backpack.tileentity.TileEntityBackpack;
import limingzxc.shulkerbox.block.BlockShulkerBox;
import limingzxc.shulkerbox.tileentity.TileEntityShulkerBox;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import net.xiaoyu233.fml.FishModLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

public class PreviewGui extends GuiScreen {
    protected static final ResourceLocation background = new ResourceLocation("preview:textures/gui/container/preview.png");
    private  boolean isSendPacket;
    public static int x,y,z;
    /**
     * Stacks renderer. Icons, stack size, health, etc...
     */
    protected static RenderItem itemRenderer = new RenderItem();

    public PreviewGui(){}

    public void drawBackground(int x, int y) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        float test = this.zLevel;
        this.zLevel = 300.0f;
        this.drawTexturedModalRect(x, y,0, 0, 176, 68);//前面两个屏幕坐标，中间两个纹理起点坐标，后面两个是长宽
        this.zLevel = test;
    }

    public void drawItems(int x, int y, ItemStack[] itemStacks) {
        int index = 0;
        for (int i = 0; i < 3; i++) {//行
            for (int j = 0; j < 9; j++) {//列
                ItemStack stack = itemStacks[index];
                if (stack != null) {
                    drawItemStack(stack, 8 + x + j * 18, 6+ y + i * 18);
                }
                index++;
            }
        }
    }

    private void drawItemStack(ItemStack par1ItemStack, int x, int y) {
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glPushMatrix();
        GL11.glEnable(32826);

        short var6 = 240;
        short var7 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.zLevel = 300.1F;
        itemRenderer.zLevel = 300.1F;
        itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), par1ItemStack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), par1ItemStack, x, y );
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;

        GL11.glPopMatrix();
    }

    public final void renderAll() {
        if((Keyboard.isKeyDown(Keyboard.KEY_LMENU)||(Keyboard.isKeyDown(Keyboard.KEY_RMENU))))
        {
            RaycastCollision rc = Minecraft.getMinecraft().objectMouseOver;
            Boolean isContainer = false;
            if (rc == null) return;
            if(rc.isBlock()) {
                ItemStack[] chestContents = new ItemStack[27];
                Block block = rc.getBlockHit();
                if(block.hasTileEntity()) {
                    TileEntity tileEntity = rc.world.getBlockTileEntity(rc.block_hit_x,rc.block_hit_y,rc.block_hit_z);
                    if(tileEntity instanceof IInventory) {
                        if (x != rc.block_hit_x || y != rc.block_hit_y || z != rc.block_hit_z) {
                            x = rc.block_hit_x;
                            y = rc.block_hit_y;
                            z = rc.block_hit_z;
                            isSendPacket = true;
                        } else {
                            isSendPacket = false;
                        }
                        if (isSendPacket) {
                            Network.sendToServer(new C2SGetInventory(rc.block_hit_x, rc.block_hit_y, rc.block_hit_z));
                        }
                        IInventory inventory = C2SGetInventory.inventory;

                        if(inventory != null) {
                            isContainer = true;
                            for (int i = 0; i < Math.min(inventory.getSizeInventory(),27); i++) {
                                chestContents[i] = inventory.getStackInSlot(i);
                            }
                        }
                    } else if (block instanceof BlockEnderChest) {
                        IInventory inventory = C2SInform.getInventoryEnderChest();
                        if(inventory != null) {
                            isContainer = true;
                            for (int i = 0; i < 27; i++) {
                                chestContents[i] = inventory.getStackInSlot(i);
                            }
                        }
                    }
                    if(isContainer) {
                        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                        int screenWidth = scaledResolution.getScaledWidth();
                        int screenHeight = scaledResolution.getScaledHeight();
                        drawBackground((screenWidth - 176) / 2, (screenHeight - 108) / 2);
                        drawItems((screenWidth - 176) / 2, (screenHeight - 108) / 2, chestContents);
                    }
                }

            }
        }
    }

}
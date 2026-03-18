package com.github.FlyBird.containerpreview;

import com.github.FlyBird.containerpreview.network.C2S.C2SGetInventory;
import com.github.FlyBird.containerpreview.network.C2S.C2SInform;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class PreviewGui extends GuiScreen {

    private static final ResourceLocation SLOT_TEXTURE = new ResourceLocation("containerpreview:textures/gui/container/slot.png");

    private static final int SLOT_SIZE = 18;
    private static final int MAX_COLUMNS = 19;
    private static final int PADDING = 4;

    private boolean isSendPacket;

    public static int x;
    public static int y;
    public static int z;

    protected static RenderItem itemRenderer = new RenderItem();

    public PreviewGui() {}

    public static ItemStack[] snapshot(IInventory inventory)
    {
        int size = Math.max(0, inventory.getSizeInventory());
        ItemStack[] snapshot = new ItemStack[size];

        for (int i = 0; i < size; i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            snapshot[i] = stack == null ? null : stack.copy();
        }

        return snapshot;
    }

    public static int getPreviewColumns(int slotCount)
    {
        if (slotCount <= 0)
        {
            return 0;
        }

        if (slotCount <= 9)
        {
            return slotCount;
        }

        if (slotCount % 9 == 0 && slotCount <= 54)
        {
            return 9;
        }

        if (slotCount % 19 == 0)
        {
            return 19;
        }

        int columns = (int) Math.ceil(Math.sqrt(slotCount));
        return Math.max(1, Math.min(MAX_COLUMNS, columns));
    }

    public static int getPreviewRows(int slotCount)
    {
        int columns = getPreviewColumns(slotCount);

        if (columns <= 0)
        {
            return 0;
        }

        return (slotCount + columns - 1) / columns;
    }

    public static int getPreviewWidth(int slotCount) {
        return getPreviewColumns(slotCount) * SLOT_SIZE;
    }

    public static int getPreviewHeight(int slotCount) {
        return getPreviewRows(slotCount) * SLOT_SIZE;
    }

    public static int getScaledPreviewWidth(int slotCount) {
        return Math.round(getPreviewWidth(slotCount) * CPConfigs.getPreviewScale());
    }

    public static int getScaledPreviewHeight(int slotCount) {
        return Math.round(getPreviewHeight(slotCount) * CPConfigs.getPreviewScale());
    }

    private void drawSlot(int x, int y) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + SLOT_SIZE, this.zLevel, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x + SLOT_SIZE, y + SLOT_SIZE, this.zLevel, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x + SLOT_SIZE, y, this.zLevel, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, this.zLevel, 0.0D, 0.0D);
        tessellator.draw();
    }

    public void drawBackground(int x, int y, int slotCount)
    {
        int columns = getPreviewColumns(slotCount);
        int rows = getPreviewRows(slotCount);

        if (columns <= 0 || rows <= 0)
        {
            return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, CPConfigs.getPreviewAlpha());
        Minecraft.getMinecraft().getTextureManager().bindTexture(SLOT_TEXTURE);
        float previousZ = this.zLevel;
        this.zLevel = 300.0f;

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < columns; col++)
            {
                this.drawSlot(x + col * SLOT_SIZE, y + row * SLOT_SIZE);
            }
        }

        this.zLevel = previousZ;
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void drawBackground(int x, int y) {
        drawBackground(x, y, 27);
    }

    public void drawItems(int x, int y, ItemStack[] itemStacks)
    {
        if (itemStacks == null || itemStacks.length == 0)
        {
            return;
        }

        int columns = getPreviewColumns(itemStacks.length);

        for (int i = 0; i < itemStacks.length; i++)
        {
            ItemStack stack = itemStacks[i];

            if (stack != null)
            {
                int row = i / columns;
                int col = i % columns;
                drawItemStack(stack, x + col * SLOT_SIZE + 1, y + row * SLOT_SIZE + 1);
            }
        }
    }

    public void drawPreview(int x, int y, ItemStack[] itemStacks)
    {
        if (itemStacks == null || itemStacks.length == 0)
        {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        float previewScale = CPConfigs.getPreviewScale();
        GL11.glScalef(previewScale, previewScale, 1.0F);
        drawBackground(0, 0, itemStacks.length);
        drawItems(0, 0, itemStacks);
        GL11.glPopMatrix();
    }

    private void drawItemStack(ItemStack stack, int x, int y)
    {
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        RenderHelper.enableGUIStandardItemLighting();

        GL11.glPushMatrix();
        GL11.glEnable(32826);

        short var6 = 240;
        short var7 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var6 / 1.0F, (float) var7 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.zLevel = 300.1F;
        itemRenderer.zLevel = 300.1F;
        itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), stack, x, y);
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;

        GL11.glPopMatrix();
    }

    public final void renderAll()
    {
        if (!(Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)))
        {
            return;
        }

        RaycastCollision raycastCollision = Minecraft.getMinecraft().objectMouseOver;

        if (raycastCollision == null || !raycastCollision.isBlock())
        {
            return;
        }

        Block block = raycastCollision.getBlockHit();

        if (!block.hasTileEntity())
        {
            return;
        }

        ItemStack[] chestContents = null;
        TileEntity tileEntity = raycastCollision.world.getBlockTileEntity(raycastCollision.block_hit_x, raycastCollision.block_hit_y, raycastCollision.block_hit_z);

        if (tileEntity instanceof IInventory)
        {
            if (x != raycastCollision.block_hit_x || y != raycastCollision.block_hit_y || z != raycastCollision.block_hit_z)
            {
                x = raycastCollision.block_hit_x;
                y = raycastCollision.block_hit_y;
                z = raycastCollision.block_hit_z;
                isSendPacket = true;
            }
            else
            {
                isSendPacket = false;
            }

            if (isSendPacket)
            {
                Network.sendToServer(new C2SGetInventory(raycastCollision.block_hit_x, raycastCollision.block_hit_y, raycastCollision.block_hit_z));
            }

            IInventory inventory = C2SGetInventory.getInventory(raycastCollision.block_hit_x, raycastCollision.block_hit_y, raycastCollision.block_hit_z);

            if (inventory != null)
            {
                chestContents = snapshot(inventory);
            }
        }

        else if (block instanceof BlockEnderChest)
        {
            if (x != raycastCollision.block_hit_x || y != raycastCollision.block_hit_y || z != raycastCollision.block_hit_z)
            {
                x = raycastCollision.block_hit_x;
                y = raycastCollision.block_hit_y;
                z = raycastCollision.block_hit_z;
                isSendPacket = true;
            }
            else
            {
                isSendPacket = false;
            }

            if (isSendPacket)
            {
                Network.sendToServer(new C2SInform());
            }
            IInventory inventory = C2SInform.getInventoryEnderChest();

            if (inventory != null)
            {
                chestContents = snapshot(inventory);
            }
        }

        if (chestContents == null || chestContents.length == 0)
        {
            return;
        }

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();
        int width = getScaledPreviewWidth(chestContents.length);
        int height = getScaledPreviewHeight(chestContents.length);

        int drawX = (screenWidth - width) / 2;
        int drawY = (screenHeight - height) / 2;
        drawX = Math.max(PADDING, Math.min(drawX, screenWidth - width - PADDING));
        drawY = Math.max(PADDING, Math.min(drawY, screenHeight - height - PADDING));
        drawPreview(drawX, drawY, chestContents);
    }
}

/*
 * NetherEx
 * Copyright (c) 2016-2019 by LogicTechCorp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package logictechcorp.netherex.client.render.tileentity;

import logictechcorp.netherex.tileentity.TileEntityUrnOfSorrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RenderUrnOfSorrow extends TileEntitySpecialRenderer<TileEntityUrnOfSorrow>
{
    @Override
    public void render(TileEntityUrnOfSorrow urn, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if(urn == null)
        {
            return;
        }

        ItemStack stack = urn.getInventory().getStackInSlot(0);

        if(!stack.isEmpty())
        {
            World world = urn.getWorld();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5F, y + 1.225F, z + 0.5F);
            GlStateManager.disableLighting();

            GlStateManager.rotate(((world.getTotalWorldTime() + partialTicks) / 15.0F) * (180.0F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.pushAttrib();

            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();

            GlStateManager.popAttrib();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}

/*
 * Copyright © 2021 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of midnightcontrols.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package eu.midnightdust.midnightcontrols.client.gui;

import dev.lambdaurora.spruceui.widget.SpruceTexturedButtonWidget;
import eu.midnightdust.midnightcontrols.client.HudSide;
import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.MidnightControlsConfig;
import eu.midnightdust.midnightcontrols.client.util.KeyBindingAccessor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X;
import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y;

/**
 * Represents the touchscreen overlay
 */
public class TouchscreenOverlay extends Screen {
    public static final Identifier                 WIDGETS_LOCATION     = new Identifier("midnightcontrols", "textures/gui/widgets.png");
    private             MidnightControlsClient       mod;
    private             SpruceTexturedButtonWidget jumpButton;
    private             SpruceTexturedButtonWidget flyButton;
    private             SpruceTexturedButtonWidget flyUpButton;
    private             SpruceTexturedButtonWidget flyDownButton;
    private             int                        flyButtonEnableTicks = 0;
    private             int                        forwardButtonTick    = 0;
    private             SpruceTexturedButtonWidget forwardLeftButton;
    private             SpruceTexturedButtonWidget forwardRightButton;
    private             SpruceTexturedButtonWidget startSneakButton;
    private             SpruceTexturedButtonWidget endSneakButton;

    public TouchscreenOverlay(@NotNull MidnightControlsClient mod)
    {
        super(Text.literal("Touchscreen overlay"));
        this.mod = mod;
        this.passEvents = true;
    }

//    @Override
//    public boolean shouldPause()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
//    {
//        super.keyPressed(keyCode,scanCode,modifiers);
//        //return false;
//        return true;
//    }
//
//    private void pauseGame(boolean bl)
//    {
//        if (this.client == null)
//            return;
//        boolean bl2 = this.client.isIntegratedServerRunning() && !this.client.getServer().isRemote();
//        if (bl2) {
//            this.client.setScreen(new GameMenuScreen(!bl));
//            this.client.getSoundManager().pauseAll();
//        } else {
//            this.client.setScreen(new GameMenuScreen(true));
//        }
//    }
//
//    /**
//     * Updates the forward button ticks cooldown.
//     *
//     * @param state The button state.
//     *
//     */
//    private void updateForwardButtonsState(boolean state)
//    {
//        if (state)
//            this.forwardButtonTick = -1;
//        else
//            this.forwardButtonTick = 20;
//    }
//
//    /**
//     * Updates the jump buttons.
//     */
//    private void updateJumpButtons()
//    {
//        if (this.client == null)
//            return;
//        if (!this.client.interactionManager.isFlyingLocked()) {
//            boolean oldStateFly = this.flyButton.visible;
//            this.jumpButton.visible = false;
//            this.flyButton.visible = true;
//            this.flyUpButton.visible = true;
//            this.flyDownButton.visible = true;
//            if (oldStateFly != this.flyButton.visible) {
//                this.flyButtonEnableTicks = 5;
//                this.handleJump(null, false);
//            } else if (this.flyButtonEnableTicks > 0)
//                this.flyButtonEnableTicks--;
//        } else {
//            this.jumpButton.visible = true;
//            this.flyButton.visible = false;
//            this.flyUpButton.visible = false;
//            this.flyDownButton.visible = false;
//        }
//    }
//
//    /**
//     * Handles the jump button.
//     *
//     * @param btn   The pressed button.
//     * @param state The state of the jump button.
//     */
//    private void handleJump(ButtonWidget btn, boolean state)
//    {
//        ((KeyBindingAccessor) this.client.options.keyJump).midnightcontrols$handlePressState(state);
//    }
//
//    @Override
//    public void tick()
//    {
//        if (this.forwardButtonTick > 0) {
//            this.forwardButtonTick--;
//        } else if (this.forwardButtonTick == 0) {
//            if (this.forwardLeftButton.visible)
//                this.forwardLeftButton.visible = false;
//            if (this.forwardRightButton.visible)
//                this.forwardRightButton.visible = false;
//        }
//        this.updateJumpButtons();
//    }
//
//    @Override
//    protected void init()
//    {
//        super.init();
//        int scaledWidth = this.client.getWindow().getScaledWidth();
//        int scaledHeight = this.client.getWindow().getScaledHeight();
//        this.addDrawableChild(new TexturedButtonWidget(scaledWidth / 2 - 20, 0, 20, 20, 0, 106, 20, ButtonWidget.WIDGETS_LOCATION, 256, 256,
//                btn -> this.client.setScreen(new ChatScreen("")), LiteralText.EMPTY));
//        this.addDrawableChild(new TexturedButtonWidget(scaledWidth / 2, 0, 20, 20, 0, 0, 20, WIDGETS_LOCATION, 256, 256,
//                btn -> this.pauseGame(false)));
//        // Inventory buttons.
//        int inventoryButtonX = scaledWidth / 2;
//        int inventoryButtonY = scaledHeight - 16 - 5;
//        if (this.client.options.mainArm == Arm.LEFT) {
//            inventoryButtonX = inventoryButtonX - 91 - 24;
//        } else {
//            inventoryButtonX = inventoryButtonX + 91 + 4;
//        }
//        this.addDrawableChild(new TexturedButtonWidget(inventoryButtonX, inventoryButtonY, 20, 20, 20, 0, 20, WIDGETS_LOCATION, 256, 256,
//                btn -> {
//                    if (this.client.interactionManager.hasRidingInventory()) {
//                        this.client.player.openRidingInventory();
//                    } else {
//                        this.client.getTutorialManager().onInventoryOpened();
//                        this.client.openScreen(new InventoryScreen(this.client.player));
//                    }
//                }));
//        int jumpButtonX, swapHandsX, sneakButtonX;
//        int sneakButtonY = scaledHeight - 10 - 40 - 5;
//        if (MidnightControlsConfig.hudSide == HudSide.LEFT) {
//            jumpButtonX = scaledWidth - 20 - 20;
//            swapHandsX = jumpButtonX - 5 - 40;
//            sneakButtonX = 10 + 20 + 5;
//        } else {
//            jumpButtonX = 20;
//            swapHandsX = jumpButtonX + 5 + 40;
//            sneakButtonX = scaledWidth - 10 - 40 - 5;
//        }
//        // Swap items hand.
//        this.addDrawableChild(new SpruceTexturedButtonWidget(swapHandsX, sneakButtonY, 20, 20, 0, 160, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    if (state) {
//                        if (!this.client.player.isSpectator()) {
//                            this.client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
//                        }
//                    }
//                }));
//        // Drop
//        this.addDrawableChild(new SpruceTexturedButtonWidget(swapHandsX, sneakButtonY + 5 + 20, 20, 20, 20, 160, 20, WIDGETS_LOCATION,
//                (btn, state) -> ((KeyBindingAccessor) this.client.options.keyDrop).midnightcontrols$handlePressState(state)));
//        // Jump keys
//        this.addDrawableChild(this.jumpButton = new SpruceTexturedButtonWidget(jumpButtonX, sneakButtonY, 20, 20, 0, 40, 20, WIDGETS_LOCATION,
//                this::handleJump));
//        this.addDrawableChild(this.flyButton = new SpruceTexturedButtonWidget(jumpButtonX, sneakButtonY, 20, 20, 20, 40, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    if (this.flyButtonEnableTicks == 0) this.client..abilities.flying = false;
//                }));
//        this.addDrawableChild(this.flyUpButton = new SpruceTexturedButtonWidget(jumpButtonX, sneakButtonY - 5 - 20, 20, 20, 40, 40, 20, WIDGETS_LOCATION,
//                this::handleJump));
//        this.addDrawableChild(this.flyDownButton = new SpruceTexturedButtonWidget(jumpButtonX, sneakButtonY + 20 + 5, 20, 20, 60, 40, 20, WIDGETS_LOCATION,
//                (btn, state) -> ((KeyBindingAccessor) this.client.options.keySneak).midnightcontrols$handlePressState(state)));
//        this.updateJumpButtons();
//        // Movements keys
//        this.addDrawableChild((this.startSneakButton = new SpruceTexturedButtonWidget(sneakButtonX, sneakButtonY, 20, 20, 0, 120, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    if (state) {
//                        ((KeyBindingAccessor) this.client.options.keySneak).midnightcontrols$handlePressState(true);
//                        this.startSneakButton.setVisible(false);
//                        this.endSneakButton.setVisible(true);
//                    }
//                })));
//        this.addDrawableChild((this.endSneakButton = new SpruceTexturedButtonWidget(sneakButtonX, sneakButtonY, 20, 20, 20, 120, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    if (state) {
//                        ((KeyBindingAccessor) this.client.options.keySneak).midnightcontrols$handlePressState(false);
//                        this.endSneakButton.setVisible(false);
//                        this.startSneakButton.setVisible(true);
//                    }
//                })));
//        this.endSneakButton.setVisible(false);
//        this.addDrawableChild(this.forwardLeftButton = new SpruceTexturedButtonWidget(sneakButtonX - 20 - 5, sneakButtonY - 5 - 20, 20, 20, 80, 80, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    ((KeyBindingAccessor) this.client.options.keyForward).midnightcontrols$handlePressState(state);
//                    ((KeyBindingAccessor) this.client.options.keyLeft).midnightcontrols$handlePressState(state);
//                    this.updateForwardButtonsState(state);
//                }));
//        this.forwardLeftButton.setVisible(false);
//        this.addDrawableChild(new SpruceTexturedButtonWidget(sneakButtonX, sneakButtonY - 5 - 20, 20, 20, 0, 80, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    ((KeyBindingAccessor) this.client.options.keyForward).midnightcontrols$handlePressState(state);
//                    this.updateForwardButtonsState(state);
//                    this.forwardLeftButton.setVisible(true);
//                    this.forwardRightButton.setVisible(true);
//                }));
//        this.addDrawableChild(this.forwardRightButton = new SpruceTexturedButtonWidget(sneakButtonX + 20 + 5, sneakButtonY - 5 - 20, 20, 20, 100, 80, 20, WIDGETS_LOCATION,
//                (btn, state) -> {
//                    ((KeyBindingAccessor) this.client.options.keyForward).midnightcontrols$handlePressState(state);
//                    ((KeyBindingAccessor) this.client.options.keyRight).midnightcontrols$handlePressState(state);
//                    this.updateForwardButtonsState(state);
//                }));
//        this.forwardRightButton.setVisible(true);
//        this.addDrawableChild(new SpruceTexturedButtonWidget(sneakButtonX + 20 + 5, sneakButtonY, 20, 20, 20, 80, 20, WIDGETS_LOCATION,
//                (btn, state) -> ((KeyBindingAccessor) this.client.options.keyRight).midnightcontrols$handlePressState(state)));
//        this.addDrawableChild(new SpruceTexturedButtonWidget(sneakButtonX, sneakButtonY + 20 + 5, 20, 20, 40, 80, 20, WIDGETS_LOCATION,
//                (btn, state) -> ((KeyBindingAccessor) this.client.options.keyBack).midnightcontrols$handlePressState(state)));
//        this.addDrawableChild(new SpruceTexturedButtonWidget(sneakButtonX - 20 - 5, sneakButtonY, 20, 20, 60, 80, 20, WIDGETS_LOCATION,
//                (btn, state) -> ((KeyBindingAccessor) this.client.options.keyLeft).midnightcontrols$handlePressState(state)));
//
//        this.children().forEach(button -> {
//            if (button instanceof SpruceTexturedButtonWidget) {
//                ((SpruceTexturedButtonWidget) button).setSilent(true);
//            }
//        });
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button)
//    {
//        if (mouseY >= (double) (this.height - 22) && this.client != null && this.client.player != null) {
//            int centerX = this.width / 2;
//            if (mouseX >= (double) (centerX - 90) && mouseX <= (double) (centerX + 90)) {
//                for (int slot = 0; slot < 9; ++slot) {
//                    int slotX = centerX - 90 + slot * 20 + 2;
//                    if (mouseX >= (double) slotX && mouseX <= (double) (slotX + 20)) {
//                        this.client.player.getInventory().selectedSlot = slot;
//                        return true;
//                    }
//                }
//            }
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    @Override
//    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
//    {
//        if (button == GLFW.GLFW_MOUSE_BUTTON_1 && this.client != null) {
//            if (deltaY > 0.01)
//                this.mod.input.handleLook(this.client, GLFW_GAMEPAD_AXIS_RIGHT_Y, (float) Math.abs(deltaY / 5.0), 2);
//            else if (deltaY < 0.01)
//                this.mod.input.handleLook(this.client, GLFW_GAMEPAD_AXIS_RIGHT_Y, (float) Math.abs(deltaY / 5.0), 1);
//
//            if (deltaX > 0.01)
//                this.mod.input.handleLook(this.client, GLFW_GAMEPAD_AXIS_RIGHT_X, (float) Math.abs(deltaX / 5.0), 2);
//            else if (deltaX < 0.01)
//                this.mod.input.handleLook(this.client, GLFW_GAMEPAD_AXIS_RIGHT_X, (float) Math.abs(deltaX / 5.0), 1);
//        }
//        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
//    }
}

package com.nativeplayer.ui.login;

import androidx.compose.animation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusManager;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.unit.Dp;
import com.nativeplayer.ui.theme.*;
import androidx.navigation.NavController;
import com.nativeplayer.ui.navigation.Screen;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000V\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u001a\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a\u0018\u0010\t\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0003\u001a&\u0010\f\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a.\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u00132\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00010\u00152\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a0\u0010\u0016\u001a\u00020\u00012\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00010\u001c2\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0018\u0010\u001d\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0003\u001a\b\u0010\u001e\u001a\u00020\u001fH\u0007\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006 "}, d2 = {"AppLogo", "", "modifier", "Landroidx/compose/ui/Modifier;", "LoginScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/login/LoginViewModel;", "M3UForm", "focusManager", "Landroidx/compose/ui/focus/FocusManager;", "PlayCircleLogo", "size", "Landroidx/compose/ui/unit/Dp;", "PlayCircleLogo-3ABfNKs", "(Landroidx/compose/ui/Modifier;F)V", "SegmentedControl", "selectedTab", "Lcom/nativeplayer/ui/login/LoginTab;", "onTabSelected", "Lkotlin/Function1;", "SegmentedTab", "text", "", "selected", "", "onClick", "Lkotlin/Function0;", "XtreamForm", "loginTextFieldColors", "Landroidx/compose/material3/TextFieldColors;", "app_debug"})
public final class LoginScreenKt {
    
    /**
     * Branding: Play-Circle emblem replaces the "O" in "Osi Player".
     * Renders as: [▶ Play-Kreis] + "si" + " Player"
     */
    @androidx.compose.runtime.Composable()
    public static final void AppLogo(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SegmentedControl(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.login.LoginTab selectedTab, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nativeplayer.ui.login.LoginTab, kotlin.Unit> onTabSelected, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SegmentedTab(java.lang.String text, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.material3.TextFieldColors loginTextFieldColors() {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LoginScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.login.LoginViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void XtreamForm(com.nativeplayer.ui.login.LoginViewModel viewModel, androidx.compose.ui.focus.FocusManager focusManager) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void M3UForm(com.nativeplayer.ui.login.LoginViewModel viewModel, androidx.compose.ui.focus.FocusManager focusManager) {
    }
}
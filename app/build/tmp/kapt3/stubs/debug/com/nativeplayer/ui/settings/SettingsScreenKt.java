package com.nativeplayer.ui.settings;

import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.ImeAction;
import androidx.compose.ui.text.input.KeyboardType;
import androidx.compose.ui.text.input.PasswordVisualTransformation;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.navigation.NavController;
import com.nativeplayer.data.model.Account;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.splash.SplashViewModel;
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel;
import com.nativeplayer.ui.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a$\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\b\u0010\u0011\u001a\u00020\u0012H\u0007\u00a8\u0006\u0013"}, d2 = {"AccountDialog", "", "title", "", "initialAccount", "Lcom/nativeplayer/data/model/Account;", "onDismiss", "Lkotlin/Function0;", "onSave", "Lkotlin/Function1;", "SettingsScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/splash/SplashViewModel;", "categoryVisibilityViewModel", "Lcom/nativeplayer/ui/viewmodel/CategoryVisibilityViewModel;", "darkTextFieldColors", "Landroidx/compose/material3/TextFieldColors;", "app_debug"})
public final class SettingsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SettingsScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.splash.SplashViewModel viewModel, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel categoryVisibilityViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void AccountDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.Account initialAccount, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nativeplayer.data.model.Account, kotlin.Unit> onSave) {
    }
    
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.material3.TextFieldColors darkTextFieldColors() {
        return null;
    }
}
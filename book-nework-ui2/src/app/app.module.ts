// import { LoginComponent } from './pages/login/login.component';
// import { RegisterComponent } from './pages/register/register.component';
// import { FormsModule } from '@angular/forms';
// import { HttpTokenInterceptor } from './services/interceptor/http-token.interceptor';
// import { ActivateAccountComponent } from './pages/activate-account/activate-account.component';
// import { CodeInputModule } from 'angular-code-input';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  provideHttpClient,
  HttpClient,
  HttpClientModule,
  withInterceptors,
  withRequestsMadeViaParent
} from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [
    provideHttpClient(),
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

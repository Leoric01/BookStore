import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {provideHttpClient, HttpClientModule, HTTP_INTERCEPTORS, withFetch} from '@angular/common/http';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule } from '@angular/forms';
import { CodeInputModule } from 'angular-code-input';
import { RegisterComponent } from './pages/register/register.component';
import { ActivateAccountComponent } from './pages/activate-account/activate-account.component';
// import { HttpTokenInterceptor } from './services/interceptor/http-token.interceptor';
// import { ActivateAccountComponent } from './pages/activate-account/activate-account.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ActivateAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CodeInputModule
  ],
  providers: [
    provideHttpClient(withFetch()),
    // {
    // provide: HTTP_INTERCEPTORS,
    // useClass: HttpTokenInterceptor,
    // multi: true
    // },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

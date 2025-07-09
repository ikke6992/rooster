import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { ModalComponent } from './modal/modal.component';
import { LoginComponent } from './login/login.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    ModalComponent,
    LoginComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'frontend';

  isLoggedIn: boolean = localStorage.getItem('token') !== null;
  currentLanguage = 'en';
  feedbackMsg: string = '';

  constructor(public translate: TranslateService) {
    translate.addLangs(['en', 'nl']);
    translate.setDefaultLang('en');

    const storedLang = localStorage.getItem('language');
    const defaultLang = storedLang || 'en';
    translate.setDefaultLang(defaultLang);
    translate.use(defaultLang);
    localStorage.setItem('language', defaultLang);
    this.currentLanguage = defaultLang;
  }

  switchLanguage(language: string) {
    this.translate.use(language);
    localStorage.setItem('language', language);
    this.currentLanguage = language;
  }

  showModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }

  logout() {
    localStorage.clear();
    window.location.reload();
  }
}

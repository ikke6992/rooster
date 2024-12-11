import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import {
  HttpEvent,
  HttpHandlerFn,
  HttpRequest,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { routes } from './app.routes';
import { Observable } from 'rxjs';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([tokenInterceptor])),
  ],
};

export function tokenInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  const token: string | null = localStorage.getItem('token')
  if (!token) {
    return next(req);
  }

  if (tokenExpired(token)) {
    localStorage.clear()
    setFeedback("Session expired, please log out and relog in")
    showModal('feedback-main')
    return next(req);
  }

  const authToken: string = `Bearer ${token}`;
  
  const reqWithToken = req.clone({
    setHeaders: {
      Authorization: authToken,
    },
  });  
  return next(reqWithToken);
}

const tokenExpired = (token: string) => {
  const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
  return (Math.floor((new Date).getTime() / 1000)) >= expiry;
}

function showModal(name: string) {
  let modal_t = document.getElementById(name);
  if (modal_t !== null) {
    modal_t.classList.remove('hhidden');
    modal_t.classList.add('sshow');
  }
}

const setFeedback = (message: string) => {
  document.getElementById('feedback-message')?.append(message)
}
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const loggedInGuard: CanActivateFn = (route, state) => {
  const loggedIn = localStorage.getItem('token') !== null;
  if (loggedIn) {
    return true;
  }
  return inject(Router).createUrlTree(['rooster']);
};

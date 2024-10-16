import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlFields = 'http://localhost:8080/api/v1/fields';

  constructor(private http: HttpClient) {}

  getFields(): Observable<any> {
    return this.http.get<any>(this.apiUrlFields);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlGroups = 'http://localhost:8080/api/v1/groups';

  constructor(private http: HttpClient) {}

  getGroups(): Observable<any> {
    return this.http.get<any>(this.apiUrlGroups + '/archived');
  }
}
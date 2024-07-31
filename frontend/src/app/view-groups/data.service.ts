import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private getFieldsUrl = 'http://localhost:8080/api/v1/fields/';
  private getGroupsUrl = 'http://localhost:8080/api/v1/groups/';
  private postGroupUrl = 'http://localhost:8080/api/v1/groups/new';

  constructor(private http: HttpClient) {}

  getFields(): Observable<any> {
    return this.http.get<any>(this.getFieldsUrl);
  }

  getGroups(): Observable<any> {
    return this.http.get<any>(this.getGroupsUrl);
  }

  postGroup(data: any): Observable<any> {
    return this.http.post<any>(this.postGroupUrl, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }
}

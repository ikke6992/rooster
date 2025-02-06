import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlFields = `${environment.apiUrl}/v1/fields`;
  private apiUrlGroups = `${environment.apiUrl}/v1/groups`;
  private apiUrlTeachers = `${environment.apiUrl}/v1/teachers`;

  constructor(private http: HttpClient) {}

  getFields(): Observable<any> {
    return this.http.get<any>(this.apiUrlFields);
  }

  getGroups(): Observable<any> {
    return this.http.get<any>(this.apiUrlGroups);
  }

  getTeachers(): Observable<any> {
    return this.http.get<any>(this.apiUrlTeachers);
  }

  postGroup(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrlGroups + '/new', data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }

  putGroup(groupNumber: number, data: any): Observable<any> {
    return this.http.put<any>(
      this.apiUrlGroups + '/' + groupNumber + '/edit',
      data,
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }

  rescheduleGroup(groupNumber: number): Observable<any> {
    return this.http.put<any>(
      this.apiUrlGroups + '/' + groupNumber + '/reschedule',
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }

  archiveGroup(groupNumber: number): Observable<any> {
    return this.http.delete<any>(
      this.apiUrlGroups + '/' + groupNumber + '/archive',
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }

  addVacation(groupNumber: number, data: any): Observable<any> {
    return this.http.put<any>(
      this.apiUrlGroups + '/' + groupNumber + '/addVacation',
      data,
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }
}

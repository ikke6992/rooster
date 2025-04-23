import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrlTeachers = `${environment.apiUrl}/api/v1/teachers`;
  private apiUrlGroups = `${environment.apiUrl}/api/v1/groups`;

  constructor(private http: HttpClient) {}

  getGroups(): Observable<any> {
    return this.http.get<any>(this.apiUrlGroups);
  }

  getTeachers(): Observable<any> {
    return this.http.get<any>(this.apiUrlTeachers);
  }

  postTeacher(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrlTeachers + '/new', data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  putAvailability(id: number, data: any): Observable<any> {
    return this.http.put<any>(this.apiUrlTeachers + '/edit/' + id, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }

  putGroup(id: number, groupId: number, data: any): Observable<any> {
    return this.http.put<any>(
      this.apiUrlTeachers + '/edit/' + id + '/addGroup/' + groupId,
      data,
      {
        headers: new HttpHeaders({ 'Content-type': 'application/json' }),
      }
    );
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private getGroupsUrl = 'http://localhost:8080/api/v1/groups/';
  private getTeachersUrl = 'http://localhost:8080/api/v1/teachers/';
  private postTeacherUrl = 'http://localhost:8080/api/v1/teachers/new';
  private putUrl = 'http://localhost:8080/api/v1/teachers/edit/';

  constructor(private http: HttpClient) {}

  getGroups(): Observable<any> {
    return this.http.get<any>(this.getGroupsUrl);
  }

  getTeachers(): Observable<any> {
    return this.http.get<any>(this.getTeachersUrl);
  }

  postTeacher(data: any): Observable<any> {
    return this.http.post<any>(this.postTeacherUrl, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  putAvailability(id: number, data: any): Observable<any> {
    return this.http.put<any>(this.putUrl + id, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }

  putGroup(id: number, groupId: number): Observable<any> {
    return this.http.put<any>(this.putUrl + id + '/addGroup/' + groupId, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }
}

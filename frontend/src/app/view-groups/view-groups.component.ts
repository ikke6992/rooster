import { Component } from '@angular/core';
import { DataService } from './data.service';
import { ModalComponent } from '../modal/modal.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { CommonModule } from '@angular/common';
import { AddVacationComponent } from './add-vacation/add-vacation.component';
import { EditGroupComponent } from './edit-group/edit-group.component';

@Component({
  selector: 'app-view-groups',
  standalone: true,
  imports: [
    CommonModule,
    ModalComponent,
    AddGroupComponent,
    AddVacationComponent,
    EditGroupComponent,
  ],
  templateUrl: './view-groups.component.html',
  styleUrl: './view-groups.component.css',
})
export class ViewGroupsComponent {
  groups: any[] = [];
  fields: any[] = [];
  teachers: any[] = [];
  BRIGHTNESS_THRESHOLD: number = 128;

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getGroups().subscribe((response: any[]) => {
      this.groups = response.sort((a,b) => a.groupNumber - b.groupNumber);
      console.log(response);
    });

    this.dataService.getTeachers().subscribe((response: any[]) => {
      this.teachers = response;
      console.log(response);
    });
  }

  reschedule(number: number) {
    if (window.confirm('Reschedule Group ' + number + '?')) {
      this.dataService.rescheduleGroup(number).subscribe(
        (response) => {
          console.log('Response:', response);
          window.location.reload();
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    }
  }

  archive(number: number) {
    if (window.confirm('Archive Group ' + number + '?')) {
      this.dataService.archiveGroup(number).subscribe(
        (response) => {
          console.log('Response:', response);
          window.location.reload();
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    }
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

  calculateBrightness(color: string): boolean{
    const r = parseInt(color.substring(1, 3), 16);
    const g = parseInt(color.substring(3, 5), 16);
    const b = parseInt(color.substring(5, 7), 16);

    const brightness = Math.sqrt(0.299*r*r + 0.587*g*g + 0.114*b*b);    
    
    return brightness < this.BRIGHTNESS_THRESHOLD;
  }
}

import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';

@Component({
  selector: 'app-set-group',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './set-group.component.html',
  styleUrl: './set-group.component.css'
})
export class SetGroupComponent {
  @Input() groups: any[] = [];
  @Input() teacher: any;

  addGroup = new FormGroup({
    setGroup: new FormControl(),
  });

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.addGroup.value;
    console.log(data);
    this.dataService.putGroup(this.teacher.id, data.setGroup).subscribe(
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

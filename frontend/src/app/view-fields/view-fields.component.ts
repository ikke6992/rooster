import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ModalComponent } from '../modal/modal.component';
import { DataService } from './data.service';

@Component({
  selector: 'app-view-fields',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  templateUrl: './view-fields.component.html',
  styleUrl: './view-fields.component.css',
})
export class ViewFieldsComponent {
  fields: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getFields().subscribe((response: any[]) => {
      this.fields = response.filter((field) => field.name !== 'Returnday');
      console.log(response);
    });
  }
}

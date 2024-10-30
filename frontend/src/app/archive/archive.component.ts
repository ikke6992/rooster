import { Component } from '@angular/core';
import { DataService } from './data.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-archive',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './archive.component.html',
  styleUrl: './archive.component.css',
})
export class ArchiveComponent {
  groups: any[] = [];

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getGroups().subscribe((response: any[]) => {
      this.groups = response;
      console.log(response);
    });
  }
}

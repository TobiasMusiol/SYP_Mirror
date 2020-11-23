import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { TestComponent } from './test/test.component';
import { TestModule } from './test/test.module';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, TestModule],
  providers: [],
  bootstrap: [AppComponent],
  // bootstrap: [TestComponent],
})
export class AppModule {}

import {AppPage} from './app.po';
import {browser, logging} from 'protractor';

describe('workspace-project App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getTitleText()).toEqual('Spoon AST visualiser');
  });

  it('textarea should be writeable', () => {
    page.navigateTo();
    page.writeText('Foo Bar');
    const textarea = page.getTextArea();
    expect(textarea).toEqual('Foo Bar');
  });

  it('should count tree size', () => {
    page.navigateTo();
    page.writeText('public class hello {}');
    const count = page.countTree();
    console.log(count);
    expect(count).toEqual(1);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
